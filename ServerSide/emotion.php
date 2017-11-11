<?php

class EmotionRecord
{
    // property declaration
    public $time = 'a default value';
    public $emotionId = "";
    public $confidence = "";
    public $image = "";

    
	public function EmotionRecord($_emotionId, $_confidence, $_image) {
        $this->emotionId = $_emotionId;
        $this->confidence = $_confidence;
        $this->image = $_image;
        $this->time = new DateTime();
    }

    public function fromJson($json)
    {
    	$obj = json_decode($json);

    	$this->emotionId = $obj->emotionId;
    	$this->confidence = $obj->confidence;
    	$this->image = $obj->image;
    	$this->time = date('c');
    }

    // method declaration
    public function toJson() {
        return json_encode($this);
    }

    public function writeToDB()
    {
    	$con=mysqli_connect("localhost","maksat","Maksat2013","reactiva");
		// Check connection
		if (mysqli_connect_errno())
		  {
		  echo "Failed to connect to MySQL: " . mysqli_connect_error();
		  }

		 $date_time = date_format($this->time, "Y-m-d H:i:s");
		// Perform queries 
		 if($stmt = $con->prepare("INSERT INTO emotion (record_time,emotion_id,confidence,image_id) VALUES (?, ?, ?, ?)"))
		 {
		 	$stmt->bind_param('sidi', 
		 								$date_time, 
		 								$this->emotionId,
		 								$this->confidence, 
		 								$this->image);
			$stmt->execute();
		 }

		mysqli_close($con);
    }
}

?>