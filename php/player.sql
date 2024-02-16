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
-- Indexes for table `player`
--
ALTER TABLE `player`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `player`
--
ALTER TABLE `player`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
