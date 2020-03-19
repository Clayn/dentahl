-- phpMyAdmin SQL Dump
-- version 4.7.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Erstellungszeit: 04. Apr 2019 um 11:00
-- Server-Version: 10.3.12-MariaDB-1:10.3.12+maria~stretch
-- PHP-Version: 7.1.17


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Element`
--

CREATE TABLE IF NOT EXISTS `Element`  (
  `id` int(10) UNSIGNED NOT NULL PRIMARY KEY,
  `name` varchar(250) NOT NULL,
  `image` varchar(1000) DEFAULT NULL
);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Ninja`
--

CREATE TABLE IF NOT EXISTS `Ninja` (
  `id` int(10) UNSIGNED NOT NULL PRIMARY KEY,
  `name` varchar(250) NOT NULL UNIQUE,
  `image` varchar(1000) DEFAULT NULL,
  `element` int(10) NOT NULL,
  `main` int(11) NOT NULL DEFAULT 0,
  FOREIGN KEY (`element`) REFERENCES `Element` (`id`)
);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Team`
--

CREATE TABLE IF NOT EXISTS `Team` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(250) NOT NULL UNIQUE,
  `description` varchar(1000) DEFAULT ''
);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `TeamNinja`
--

CREATE TABLE IF NOT EXISTS `TeamNinja` (
  `position` int(11) NOT NULL,
  `team_id` int(11) NOT NULL,
  `ninja_id` int(10) NOT NULL,
  FOREIGN KEY (`team_id`) REFERENCES `Team` (`id`),
  FOREIGN KEY (`ninja_id`) REFERENCES `Ninja` (`id`)
);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `token` varchar(255) NOT NULL
);
