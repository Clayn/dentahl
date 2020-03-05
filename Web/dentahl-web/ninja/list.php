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

function get_ninjas() {
    $SQL = "SELECT * FROM `Ninja` ORDER BY id ASC";

    $db = openConnection();
    $stmt = $db->prepare($SQL);

    $ninjas = array();
    $stmt->execute();
    $result = $stmt->get_result();
    if ($result->num_rows === 0) {
        echo json_encode($ninjas);
    } else {
        while ($row = $result->fetch_assoc()) {

            $ninja = map_to_ninja($row);
            $ninjas[] = $ninja;
        }
    }
    return $ninjas;
}

$ninjas = get_ninjas();
if (count($ninjas) >= 0) {
    echo json_encode($ninjas);
} else {
    echo "[]";
}
