<?php

class DBConfiguration {

    var $url = "host";
    var $database = "db";
    var $user = "user";
    var $password = "password";

}

function getDBConfiguration() {
    return new DBConfiguration();
}
