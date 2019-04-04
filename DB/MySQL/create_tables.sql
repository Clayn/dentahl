-- phpMyAdmin SQL Dump
-- version 4.7.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Erstellungszeit: 04. Apr 2019 um 11:00
-- Server-Version: 10.3.12-MariaDB-1:10.3.12+maria~stretch
-- PHP-Version: 7.1.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `clayn_dentahl`
--
DROP DATABASE IF EXISTS `clayn_dentahl`;
CREATE DATABASE IF NOT EXISTS `clayn_dentahl` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `clayn_dentahl`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Element`
--

DROP TABLE IF EXISTS `Element`;
CREATE TABLE `Element` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(250) NOT NULL,
  `image` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Ninja`
--

DROP TABLE IF EXISTS `Ninja`;
CREATE TABLE `Ninja` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(250) NOT NULL,
  `image` varchar(1000) DEFAULT NULL,
  `element` int(10) UNSIGNED NOT NULL,
  `main` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Team`
--

DROP TABLE IF EXISTS `Team`;
CREATE TABLE `Team` (
  `id` int(11) NOT NULL,
  `name` varchar(250) NOT NULL,
  `description` varchar(1000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Team_Ninja`
--

DROP TABLE IF EXISTS `Team_Ninja`;
CREATE TABLE `Team_Ninja` (
  `position` int(11) NOT NULL,
  `team_id` int(11) NOT NULL,
  `ninja_id` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `User`
--

DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `token` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `Element`
--
ALTER TABLE `Element`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `Ninja`
--
ALTER TABLE `Ninja`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_ninja_has_element` (`element`);

--
-- Indizes für die Tabelle `Team`
--
ALTER TABLE `Team`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indizes für die Tabelle `Team_Ninja`
--
ALTER TABLE `Team_Ninja`
  ADD KEY `fk_team_id` (`team_id`),
  ADD KEY `fk_ninja_id` (`ninja_id`);

--
-- Indizes für die Tabelle `User`
--
ALTER TABLE `User`
  ADD UNIQUE KEY `token` (`token`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `Ninja`
--
ALTER TABLE `Ninja`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `Team`
--
ALTER TABLE `Team`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `Ninja`
--
ALTER TABLE `Ninja`
  ADD CONSTRAINT `fk_ninja_has_element` FOREIGN KEY (`element`) REFERENCES `Element` (`id`);

--
-- Constraints der Tabelle `Team_Ninja`
--
ALTER TABLE `Team_Ninja`
  ADD CONSTRAINT `fk_ninja_id` FOREIGN KEY (`ninja_id`) REFERENCES `Ninja` (`id`),
  ADD CONSTRAINT `fk_team_id` FOREIGN KEY (`team_id`) REFERENCES `Team` (`id`);
