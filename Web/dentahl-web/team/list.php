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

include_once __DIR__ . '/../config.php';
include_once __DIR__ . '/../tools.php';
include_once __DIR__ . '/../domain.php';

$SQL = "SELECT * FROM `Team` ORDER BY id ASC";
$SQL_POSITIONS = "SELECT * FROM `Team_Ninja` WHERE `team_id`=?";

$db = openConnection();
$stmt = $db->prepare($SQL);

$teams = array();
$stmt->execute();
$result = $stmt->get_result();
if ($result->num_rows === 0) {
    exit('No rows');
}
while ($row = $result->fetch_assoc()) {
    $id = $row['id'];
    $name = $row['name'];
    $description = $row['description'];
    $team = new Team($name, $description, $id);
    $teams[] = $team;
}
$stmt->close();
for ($i = 0; $i < sizeof($teams); $i++) {
    $teamStat = $db->prepare($SQL_POSITIONS);
    if (!$teamStat) {
        echo "TeamStat " . $teamStat;
        echo "Error: " . mysqli_error($db);
    }
    $team = $teams[$i];
    $teamStat->bind_param("i", $team->id);
    $teamStat->execute();
    $teamResult = $teamStat->get_result();
    while ($teamRow = $teamResult->fetch_assoc()) {
        $pos = $teamRow['position'];
        $nin = $teamRow['ninja_id'];
        $team->positions[$pos] = $nin;
    }
}



echo json_encode($teams);
