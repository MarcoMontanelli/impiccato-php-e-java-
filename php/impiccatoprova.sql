-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 16, 2024 at 03:15 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `impiccatoprova`
--

-- --------------------------------------------------------

--
-- Table structure for table `game_guesses`
--

CREATE TABLE `game_guesses` (
  `id` int(11) NOT NULL,
  `game_id` int(11) DEFAULT NULL,
  `player_id` int(11) DEFAULT NULL,
  `guess` varchar(255) DEFAULT NULL,
  `is_correct` tinyint(1) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `game_hints`
--

CREATE TABLE `game_hints` (
  `id` int(11) NOT NULL,
  `game_id` int(11) DEFAULT NULL,
  `sender_id` int(11) DEFAULT NULL,
  `receiver_id` int(11) DEFAULT NULL,
  `hint` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `game_results`
--

CREATE TABLE `game_results` (
  `id` int(11) NOT NULL,
  `game_id` int(11) DEFAULT NULL,
  `winner_id` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `final_word` varchar(255) DEFAULT NULL,
  `time_taken` int(11) DEFAULT NULL,
  `hints_used` int(11) DEFAULT NULL,
  `attempts_for_current_word` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `game_scores`
--

CREATE TABLE `game_scores` (
  `id` int(11) NOT NULL,
  `game_id` int(11) DEFAULT NULL,
  `player_id` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `game_session`
--

CREATE TABLE `game_session` (
  `id` int(11) NOT NULL,
  `game_id` int(11) DEFAULT NULL,
  `player_id` int(11) DEFAULT NULL,
  `is_creator` tinyint(1) DEFAULT 0,
  `is_joined` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hangman_game`
--

CREATE TABLE `hangman_game` (
  `id` int(11) NOT NULL,
  `code` varchar(10) DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `word_to_guess` varchar(255) DEFAULT NULL,
  `is_game_active` tinyint(1) DEFAULT 0,
  `winner_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `attempts` int(11) DEFAULT NULL,
  `infinite_attempts` tinyint(1) DEFAULT 0,
  `current_word` varchar(255) DEFAULT NULL,
  `current_word_attempts` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hangman_game`
--

INSERT INTO `hangman_game` (`id`, `code`, `creator_id`, `word_to_guess`, `is_game_active`, `winner_id`, `created_at`, `updated_at`, `attempts`, `infinite_attempts`, `current_word`, `current_word_attempts`) VALUES
(1, '65ce88455f', 1, NULL, 1, NULL, '2024-02-15 21:55:17', '2024-02-15 21:55:17', 4, 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `player`
--

CREATE TABLE `player` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `total_wins` int(11) DEFAULT 0,
  `total_losses` int(11) DEFAULT 0,
  `total_score` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `player`
--

INSERT INTO `player` (`id`, `username`, `total_wins`, `total_losses`, `total_score`) VALUES
(1, 'PROVA22', 0, 0, 0),
(2, 'marco22', 0, 0, 0),
(3, 'marco224', 0, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `game_guesses`
--
ALTER TABLE `game_guesses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `game_id` (`game_id`),
  ADD KEY `player_id` (`player_id`);

--
-- Indexes for table `game_hints`
--
ALTER TABLE `game_hints`
  ADD PRIMARY KEY (`id`),
  ADD KEY `game_id` (`game_id`),
  ADD KEY `sender_id` (`sender_id`),
  ADD KEY `receiver_id` (`receiver_id`);

--
-- Indexes for table `game_results`
--
ALTER TABLE `game_results`
  ADD PRIMARY KEY (`id`),
  ADD KEY `game_id` (`game_id`),
  ADD KEY `winner_id` (`winner_id`);

--
-- Indexes for table `game_scores`
--
ALTER TABLE `game_scores`
  ADD PRIMARY KEY (`id`),
  ADD KEY `game_id` (`game_id`),
  ADD KEY `player_id` (`player_id`);

--
-- Indexes for table `game_session`
--
ALTER TABLE `game_session`
  ADD PRIMARY KEY (`id`),
  ADD KEY `game_id` (`game_id`),
  ADD KEY `player_id` (`player_id`);

--
-- Indexes for table `hangman_game`
--
ALTER TABLE `hangman_game`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `code` (`code`),
  ADD KEY `creator_id` (`creator_id`),
  ADD KEY `winner_id` (`winner_id`);

--
-- Indexes for table `player`
--
ALTER TABLE `player`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `game_guesses`
--
ALTER TABLE `game_guesses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `game_hints`
--
ALTER TABLE `game_hints`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `game_results`
--
ALTER TABLE `game_results`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `game_scores`
--
ALTER TABLE `game_scores`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `game_session`
--
ALTER TABLE `game_session`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `hangman_game`
--
ALTER TABLE `hangman_game`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `player`
--
ALTER TABLE `player`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `game_guesses`
--
ALTER TABLE `game_guesses`
  ADD CONSTRAINT `game_guesses_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `hangman_game` (`id`),
  ADD CONSTRAINT `game_guesses_ibfk_2` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`);

--
-- Constraints for table `game_hints`
--
ALTER TABLE `game_hints`
  ADD CONSTRAINT `game_hints_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `hangman_game` (`id`),
  ADD CONSTRAINT `game_hints_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `player` (`id`),
  ADD CONSTRAINT `game_hints_ibfk_3` FOREIGN KEY (`receiver_id`) REFERENCES `player` (`id`);

--
-- Constraints for table `game_results`
--
ALTER TABLE `game_results`
  ADD CONSTRAINT `game_results_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `hangman_game` (`id`),
  ADD CONSTRAINT `game_results_ibfk_2` FOREIGN KEY (`winner_id`) REFERENCES `player` (`id`);

--
-- Constraints for table `game_scores`
--
ALTER TABLE `game_scores`
  ADD CONSTRAINT `game_scores_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `hangman_game` (`id`),
  ADD CONSTRAINT `game_scores_ibfk_2` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`);

--
-- Constraints for table `game_session`
--
ALTER TABLE `game_session`
  ADD CONSTRAINT `game_session_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `hangman_game` (`id`),
  ADD CONSTRAINT `game_session_ibfk_2` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`);

--
-- Constraints for table `hangman_game`
--
ALTER TABLE `hangman_game`
  ADD CONSTRAINT `hangman_game_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `player` (`id`),
  ADD CONSTRAINT `hangman_game_ibfk_2` FOREIGN KEY (`winner_id`) REFERENCES `player` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
