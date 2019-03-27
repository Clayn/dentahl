<?php

class DBConfiguration {
    var $url="localhost";
    var $database="clayn_dentahl";
    var $user="clayn_dentahl";
    var $password="bplacedDentahl";
} 

function getDBConfiguration() {
    return new DBConfiguration();
}
