<?php
header("Access-Control-Allow-Origin: *");
header("Content-type: application/json");
require('functions.inc.php');

$output = array(
	"error" => false,
	"string" => "",
	"answer" => 0
);

try{
	$t = $_REQUEST['text'];
	if (is_null($t)){
		$output['error']=true;
		$output['string']="Text query param not passed correctly.";
		$output['answer']=-1;
		http_response_code(400);
	} else {
		$answer=wordcount($t);
		$output['string']="Contains ".$answer." words";
		$output['answer']=$answer;
	}
} catch (Error $e) {
	$output['error']=true;
	$output['string']="Unknown error encountered: ".$e;
	$output['answer']=-1;
	http_response_code(500);
} finally {
	echo json_encode($output);
}
exit();
