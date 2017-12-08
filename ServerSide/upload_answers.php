<?php
	
	ini_set('display_errors', 1);
	ini_set('display_startup_errors', 1);
	error_reporting(E_ALL);

	echo "HEEY";

	function reformatDate($dateString)
	{
		$android_date_format = 'M d, Y H:i:s';
		$mysql_date_format = "Y-m-d H:i:s";

		$date = DateTime::createFromFormat($android_date_format, $dateString);
		return $date->format($mysql_date_format);
	}

	//Make sure that it is a POST request.
	if(strcasecmp($_SERVER['REQUEST_METHOD'], 'POST') != 0){
	    throw new Exception('Request method must be POST!');
	}
	 
	//Make sure that the content type of the POST request has been set to application/json
	$contentType = isset($_SERVER["CONTENT_TYPE"]) ? trim($_SERVER["CONTENT_TYPE"]) : '';
	if(strpos($contentType, 'application/json') === false){
		print_r($contentType);
	    throw new Exception('Content type must be: application/json');
	}
	 
	//Receive the RAW post data.
	$content = trim(file_get_contents("php://input"));
	 
	//Attempt to decode the incoming RAW post data from JSON.
	$decoded = json_decode($content, true);
	
	//If json_decode failed, the JSON is invalid.
	if(!is_array($decoded)){
	    throw new Exception('Received content contained invalid JSON!');
	}
	 
	//Process the JSON.
	//print_r($decoded);


	//1. create a session with some id
	$session_id = 0;
	// Create connection
	$con=mysqli_connect("localhost","maksat","Maksat2013","reactiva");
	// Check connection
	if (mysqli_connect_errno())
	{
		throw new Exception("Failed to connect to MySQL: " . mysqli_connect_error());
	}


	$sqlSession = 
			"INSERT INTO  `reactiva`.`survey_session` (`time_start` ,`time_end` ,`user_id`)
			VALUES ( ? , ? ,  ?);";
	$session_start = reformatDate($decoded["session_start"]);
	$session_end = reformatDate($decoded["session_end"]);
	$user_id = $decoded["user_id"];

	$stmt = $con->prepare($sqlSession);
	if ( !$stmt ) {
		  throw new Exception('prepare() failed: ' . htmlspecialchars($con->error));
	}

	$rc = $stmt->bind_param("ssi", $session_start, $session_end, $user_id);
	if ( !$rc ) {
	  // again execute() is useless if you can't bind the parameters. Bail out somehow.
	  throw new Exception('bind_param() failed: ' . htmlspecialchars($stmt->error));
	}

	$rc = $stmt->execute();
	if ( !$rc ) {
		  // again execute() is useless if you can't bind the parameters. Bail out somehow.
		  throw new Exception('bind_param() failed: ' . htmlspecialchars($stmt->error));
	}
    $session_id = $con->insert_id;  

	$sqlAnswers = "INSERT INTO `reactiva`.`survey_answer` ( `session_id`, `question`, `answer`, `image_name`, `time_start`, `time_end`) VALUES (?, ?, ?, ?, ?, ?)";
	$answers = $decoded["answers"];
	
	foreach($answers as $question => $answer)
	{
		$answer_string = "";
		foreach($answer["answer_strings"] as $a)
		{
			$answer_string .= $a;
		}
		
		$image_name = $answer["image_name"];
		$date_start_string = reformatDate($answer["time_start"]);
		$date_end_string = reformatDate($answer["time_end"]);
		$stmt = $con->prepare($sqlAnswers);
		if ( false===$stmt ) {
		  throw new Exception('prepare() failed: ' . htmlspecialchars($con->error));
		}

		$rc = $stmt->bind_param("isssss", $session_id, $question, $answer_string, $image_name, $date_start_string, $date_end_string);
		if ( false===$rc ) {
		  // again execute() is useless if you can't bind the parameters. Bail out somehow.
		  throw new Exception('bind_param() failed: ' . htmlspecialchars($stmt->error));
		}
		$rc = $stmt->execute();
		if ( false===$rc ) {
		  // again execute() is useless if you can't bind the parameters. Bail out somehow.
		  throw new Exception('bind_param() failed: ' . htmlspecialchars($stmt->error));
		}
	}

	$start = time();
	$con->begin_transaction(MYSQLI_TRANS_START_READ_WRITE);
	

	$sqlEmotions = "INSERT INTO `reactiva`.`emotion` ( `session_id`, `record_time`, `emotion_id`, `confidence`, `valence`, `arousal`, `disgust`, `fear`, `joy`, `sadness`, `surprise`, `contempt`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	$emotions = $decoded["emotions"];
	
	foreach($emotions as $emotion)
	{	
		$emotion_time = reformatDate($emotion["t"]);
		$emotion_id = $emotion["i"];
		$emotion_score = $emotion["s"];
		$emotion_valence = $emotion["v"];
		$emotion_arousal = $emotion["a"];
		$emotion_disgust = $emotion["d"];
		$emotion_fear = $emotion["f"];
		$emotion_joy = $emotion["j"];
		$emotion_sadness = $emotion["sad"];
		$emotion_surprise = $emotion["surp"];
		$emotion_contempt = $emotion["cont"];

		$stmt = $con->prepare($sqlEmotions);
		if ( false===$stmt ) {
		  throw new Exception('prepare() failed: ' . htmlspecialchars($con->error));
		}

		$rc = $stmt->bind_param("isidiiiiiiii", 
			$session_id, 
			$emotion_time, 
			$emotion_id, 
			$emotion_score, 
			$emotion_valence, 
			$emotion_arousal, 
			$emotion_disgust, 
			$emotion_fear, 
			$emotion_joy, 
			$emotion_sadness, 
			$emotion_surprise, 
			$emotion_contempt);
		if ( false===$rc ) {
		  // again execute() is useless if you can't bind the parameters. Bail out somehow.
		  throw new Exception('bind_param() failed: ' . htmlspecialchars($stmt->error));
		}

		$rc = $stmt->execute();
		if ( false===$rc ) {
		  throw new Exception('execute() failed: ' . htmlspecialchars($stmt->error));
		}
	}

	$con->commit();

	$duration = time()-$start;
    printf ("inserting %d rows of test data took %0.3f seconds", count($emotions), $duration);


	$con->close();
?>