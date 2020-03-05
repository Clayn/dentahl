<?php

/*
 * The MIT License
 *
 * Copyright 2019 Clayn <clayn_osmato@gmx.de>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
header("Access-Control-Allow-Methods: POST");
include_once __DIR__ . '/../config.php';
include_once __DIR__ . '/../tools.php';
include_once __DIR__ . '/../domain.php';

function check_parameter($array, $param, $message, $error) {
    if (isset($array[$param])) {
        return true;
    } else {
        $error->message = $message;
        return false;
    }
}

function is_valid_id($ninjas, $id) {
    for ($i = 0; $i < sizeof($ninjas); $i++) {
        if ($ninjas[$i]->id == $id) {
            return true;
        }
    }
    return false;
}

function get_ninjas() {
    $SQL = "SELECT * FROM `Ninja` ORDER BY id ASC";

    $db = openConnection();
    $stmt = $db->prepare($SQL);

    $ninjas = array();
    $stmt->execute();
    $result = $stmt->get_result();
    if ($result->num_rows === 0) {
        exit('No rows');
    }
    while ($row = $result->fetch_assoc()) {

        $ninja = map_to_ninja($row);
        $ninjas[] = $ninja;
    }
    return $ninjas;
}

$err = new ErrorMessage("");
$safePost = filter_input_array(INPUT_POST);
if (!empty($safePost)) {
    if (check_parameter($safePost, "token", "missing.token", $err)) {
        if (check_parameter($safePost, "name", "missing.name", $err)) {
            $token = $safePost["token"];
            $ninjas = get_ninjas();
            $count = 0;
            $validPos = array();
            for ($i = 0; $i < 9; $i++) {
                if (isset($safePost[$i])) {
                    $nin = $safePost[$i];
                    if (is_valid_id($ninjas, $safePost[$i])) {
                        $count++;
                        $validPos[$i] = $safePost[$i];
                    }
                }
            }
            if ($count > 4 || $count <= 0) {
                echo $safePost;
                $err->message = "invalid.position: " . $count;
            } else {
                $name = $safePost["name"];
                $description = isset($safePost["description"]) ? $safePost["description"] : "";
                if (check_token($token)) {
                    $SQL = "INSERT INTO `Team`(`name`, `description`) VALUES (?,?)";
                    $db = openConnection();
                    $stmt = $db->prepare($SQL);
                    $stmt->bind_param("ss", $name, $description);
                    $stmt->execute();
                    $newId = $db->insert_id;
                    $db->close();
                    if ($newId > 0) {
                        $validKeys = array_keys($validPos);
                        foreach ($validKeys as $key) {
                            $pos = $key;
                            $nin = $validPos[$key];
                            $SQL = "INSERT INTO `Team_Ninja`(`position`, `team_id`, `ninja_id`) VALUES (?,?,?)";
                            $db = openConnection();
                            $stmt = $db->prepare($SQL);
                            $stmt->bind_param("iii", $pos, $newId, $nin);
                            $stmt->execute();
                            $db->close();
                        }
                        $err->message = "" . $newId;
                    } else {
                        $err->message = "multiple";
                    }
                } else {
                    $err->message = "invalid.token";
                }
            }
        }
    }
} else {
    $err->message = "missing.parameters";
}
echo json_encode($err);
