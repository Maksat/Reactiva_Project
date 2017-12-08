<style>

	table {
		color: #333;
		font-family: Helvetica, Arial, sans-serif;
		font-size: small;
		width: 640px;
		border-collapse:
		collapse; border-spacing: 0;
		}

	td, th {
		border: 1px solid transparent; /* No more visible border */
		height: 30px;
		transition: all 0.3s; /* Simple transition for hover effect */
	}

	th {
		background: #DFDFDF; /* Darken header a bit */
		font-weight: bold;
	}

	td {
		background: #FAFAFA;
		text-align: center;
	}

	/* Cells in even rows (2,4,6...) are one color */
	tr:nth-child(even) td { background: #F1F1F1; }

	/* Cells in odd rows (1,3,5...) are another (excludes header cells) */
	tr:nth-child(odd) td { background: #FEFEFE; }

	tr td:hover { background: #666; color: #FFF; } /* Hover cell effect! */

	#thumbwrap {
		position:relative;
	}
	.thumb img { 
		border:1px solid #000;
		margin:3px;
		float:left;
	}
	.thumb span { 
		position:absolute;
		visibility:hidden;
	}
	.thumb:hover, .thumb:hover span { 
		visibility:visible;
		top:-40px; left:20px; 
		z-index:1;
	}

</style>

<?php
	
	ini_set('display_errors', 1);
	ini_set('display_startup_errors', 1);
	error_reporting(E_ALL);

	require_once('emotion.php');
	include "libchart/classes/libchart.php";

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
		
		
		$sql_emotions = "SELECT `id`, 
								`record_time`, 
								`valence`, 
								`arousal` 								
								FROM `emotion`";
		$result_emotions = $con->query($sql_emotions);

		$emotions = array();

		if ($result_emotions->num_rows > 0) {
			$emotions = array_pad($emotions, $result_emotions->num_rows, 0);
			$i=0;
		    // output data of each row
		    while($row = $result_emotions->fetch_assoc()) {
		    	
		    	$point = array("record_time"=>strtotime($row["record_time"]), 
		    					"valence"=>$row["valence"], 
		    					"arousal"=>$row["arousal"]);
		    	$emotions[$i++]=$point;
		    }

		   
		}

		$sql = "SELECT `survey_answer`.`id`, 
					   `survey_answer`.`session_id`, 
					   `survey_answer`.`question`,  
					   `survey_answer`.`answer`, 
					   `survey_answer`.`image_name`, 
					   `survey_answer`.`time_start`,
					   `survey_answer`.`time_end`,
					   ROUND(STDDEV(`emotion`.`arousal`), 2), 
					   ROUND(STDDEV(`emotion`.`valence`), 2),
					   ROUND(STDDEV(`emotion`.`contempt`), 2),
					   ROUND(STDDEV(`emotion`.`disgust`), 2),
					   ROUND(STDDEV(`emotion`.`fear`), 2),
					   ROUND(STDDEV(`emotion`.`joy`), 2),
					   ROUND(STDDEV(`emotion`.`sadness`), 2),
					   ROUND(STDDEV(`emotion`.`surprise`), 2),
					   ROUND(AVG(`emotion`.`arousal`), 2),
					   ROUND(AVG(`emotion`.`valence`), 2),
					   ROUND(AVG(`emotion`.`contempt`), 2),
					   ROUND(AVG(`emotion`.`disgust`), 2),
					   ROUND(AVG(`emotion`.`fear`), 2),
					   ROUND(AVG(`emotion`.`joy`), 2),
					   ROUND(AVG(`emotion`.`sadness`), 2), 
					   ROUND(AVG(`emotion`.`surprise`), 2)
					   FROM `survey_answer` 
					   INNER JOIN `emotion` ON `emotion`.`record_time` BETWEEN `survey_answer`.`time_start` AND `survey_answer`.`time_end` 
					   GROUP BY `survey_answer`.`id`
					   ORDER BY `survey_answer`.`id` DESC
					   ";
		$result = $con->query($sql);

		echo "<form method=POST><input type='submit' target='#' value='refresh'/></form>";
		echo "<div style='float: left; width: 40%;'>";
		echo "<table style='border:solid 1px;'>";
		echo "<tr>
				  <th>image</th>
				  <th>session id</th>
				  <th>answer id</th>
				  <th>time start</th>
				  <th>time end</th>
				  <th>AVG arousal</th>
				  <th>STD arousal</th>
				  <th>AVG valence</th>
				  <th>STD valence</th>
				  <th>AVG contempt</th>
				  <th>STD contempt</th>
				  <th>AVG disgust</th>
				  <th>STD disgust</th>
				  <th>AVG fear</th>
				  <th>STD fear</th>
				  <th>AVG joy</th>
				  <th>STD joy</th>
				  <th>AVG sadness</th>
				  <th>STD sadness</th>
				  <th>AVG surprise</th>
				  <th>STD surprise</th>
				  <th>chart</th>
				  </tr>";


		if ($result->num_rows > 0) {
		    // output data of each row
		    while($row = $result->fetch_assoc()) {
		    	
		    	echo "<tr>";
		    	?>
		    	<td>
		    		<div id="thumbwrap">
		    			<a class="thumb" href="#">
		    				<img src="uploads/<? echo $row["image_name"]; ?>" height="42" alt="face"/>
		    				<span><img src="uploads/<? echo $row["image_name"]; ?>" height="200" alt="face"/></span>
		    			</a>
		    		</div>	
		    	</td>
		    		<?
		    	echo "<td>".$row["session_id"]."</td>";
		    	echo "<td>".$row["id"]."</td>";
		    	echo "<td>".$row["time_start"]."</td>";
		    	echo "<td>".$row["time_end"]."</td>";
		    	echo "<td>".$row["ROUND(AVG(`emotion`.`arousal`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(STDDEV(`emotion`.`arousal`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(AVG(`emotion`.`valence`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(STDDEV(`emotion`.`valence`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(AVG(`emotion`.`contempt`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(STDDEV(`emotion`.`contempt`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(AVG(`emotion`.`disgust`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(STDDEV(`emotion`.`disgust`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(AVG(`emotion`.`fear`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(STDDEV(`emotion`.`fear`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(AVG(`emotion`.`joy`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(STDDEV(`emotion`.`joy`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(AVG(`emotion`.`sadness`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(STDDEV(`emotion`.`sadness`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(AVG(`emotion`.`surprise`), 2)"]."</td>";
		    	echo "<td>".$row["ROUND(STDDEV(`emotion`.`surprise`), 2)"]."</td>";		  


		    	
		    	$chart = new LineChart(150, 62);
				$valence = new XYDataSet();
				$arousal = new XYDataSet();
				
				$time_start = strtotime($row["time_start"]);
				$time_end = strtotime($row["time_end"]);

				$point_count = 0;
				foreach($emotions as $emotion)
				{
					$record_time = $emotion["record_time"];

					if($record_time> $time_start && $record_time < $time_end)
					{
						$time_diff_seconds = $record_time - $time_start;
						$valence->addPoint(new Point($time_diff_seconds, $emotion["valence"]));
		    		 	$arousal->addPoint(new Point($time_diff_seconds, $emotion["arousal"]));
		    		 	$point_count ++;
					}
				}

				if($point_count > 0)
				{
					$dataSet = new XYSeriesDataSet();
					$dataSet->addSerie("Valence", $valence);
					$dataSet->addSerie("Arousal", $arousal);
					$chart->setDataSet($dataSet);

					$chart->setTitle("Emotions");
					$file_name = "generated/chart".$row["id"].".png";
					$chart->render($file_name);
					echo "<td><img src=\"".$file_name."\" /></td>";
				}else{
					echo "<td></td>";
				}
				
		        echo "</tr>";
		    }
		} 
		echo "</table></div>";


/*
		echo "<form method=POST><input type='submit' target='#' value='refresh'/></form>";
		echo "<div style='float: left; width: 40%;'>";
		echo "<table style='border:solid 1px;'>";
		echo "<tr><td>session</td><td>id</td><td>time</td><td>emotion</td><td>score</td><td>valence</td><td>arousal</td><td>disgust</td><td>fear</td><td>joy</td><td>sadness</td><td>surprise</td><td>contempt</td></tr>";
		if ($result->num_rows > 0) {
		    // output data of each row
		    while($row = $result->fetch_assoc()) {
		    	echo "<tr>";
		    	echo "<td>".$row["session_id"]."</td>";
		    	echo "<td>".$row["id"]."</td>";
		    	echo "<td>".$row["record_time"]."</td>";
		    	echo "<td>".$row["name"]."</td>";
		    	echo "<td>".$row["confidence"]."</td>";
		    	echo "<td>".$row["valence"]."</td>";
		    	echo "<td>".$row["arousal"]."</td>";
		    	echo "<td>".$row["disgust"]."</td>";
		    	echo "<td>".$row["fear"]."</td>";
		    	echo "<td>".$row["joy"]."</td>";
		    	echo "<td>".$row["sadness"]."</td>";
		    	echo "<td>".$row["surprise"]."</td>";
		    	echo "<td>".$row["contempt"]."</td>";
		        echo "</tr>";
		    }
		} 
		echo "</table></div>";

		echo "<div style='float: right; width:40%'> ";
		echo "<table style='border:solid 1px;'>";
		echo "<tr><td>session</td><td>id</td><td>time start</td><td>time end</td><td>question</td><td>answer</td></tr>";
		
		$sql = "SELECT `id`, `session_id`, `question`, `answer`, `time_start`, `time_end` FROM `survey_answer` ORDER BY `id` DESC";
		$result = $con->query($sql);

		if ($result->num_rows > 0) {
		    // output data of each row
		    while($row = $result->fetch_assoc()) {
		    	echo "<tr>";
		    	echo "<td>".$row["session_id"]."</td>";
		    	echo "<td>".$row["id"]."</td>";
		    	echo "<td>".$row["time_start"]."</td>";
		    	echo "<td>".$row["time_end"]."</td>";
		    	echo "<td>".$row["question"]."</td>";
		    	echo "<td>".$row["answer"]."</td>";
		        echo "</tr>";
		    }
		} 

		echo "</table></div>";
		*/
		$con->close();
	}
	
?>