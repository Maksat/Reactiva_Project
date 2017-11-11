<?php
	
	ini_set('display_errors', 1);
	ini_set('display_startup_errors', 1);
	error_reporting(E_ALL);

	require_once('emotion.php');
	echo "<h1>Reaciva results</h1>";

	if(isset($_POST["emotionId"]))
	{
		$emotion = new EmotionRecord($_POST["emotionId"],$_POST["confidence"],$_POST["image"]);
		
		echo "emotion: \r\n";
		echo $emotion->toJson();

		$emotion->writeToDB();
	}
	else
	{
		$con=mysqli_connect("localhost","maksat","Maksat2013","reactiva");
		// Check connection
		if (mysqli_connect_errno())
		{
		  echo "Failed to connect to MySQL: " . mysqli_connect_error();
		}
		
		$sql = "SELECT emotion.id, record_time, image_id, emotion_name.name, confidence FROM emotion INNER JOIN emotion_name ON emotion.emotion_id = emotion_name.id ORDER BY emotion.id DESC";
		$result = $con->query($sql);

		echo "<form method=POST><input type='submit' target='#' value='refresh'/></form>";
		echo "<table style='border:solid 1px;'>";
		echo "<tr><td>id</td><td>time</td><td>image id</td><td>emotion id</td><td>confidence</td></tr>";
		if ($result->num_rows > 0) {
		    // output data of each row
		    while($row = $result->fetch_assoc()) {
		    	echo "<tr>";
		    	echo "<td>".$row["id"]."</td>";
		    	echo "<td>".$row["record_time"]."</td>";
		    	echo "<td>".$row["image_id"]."</td>";
		    	echo "<td>".$row["name"]."</td>";
		    	echo "<td>".$row["confidence"]."</td>";
		        echo "</tr>";
		    }
		} 
		echo "</table>";
		$con->close();
	}
?>