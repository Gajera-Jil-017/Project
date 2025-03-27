-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 31, 2024 at 09:04 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hotel`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `addRoomBooking` (IN `p_roomNumber` INT, IN `p_customerID` INT, IN `p_checkInDate` DATE, IN `p_checkOutDate` DATE, IN `p_roomType` VARCHAR(50), IN `p_price` DOUBLE)   BEGIN
    INSERT INTO RoomBooking (RoomNumber, CustomerID, CheckInDate, CheckOutDate, RoomType, Price)
    VALUES (p_roomNumber, p_customerID, p_checkInDate, p_checkOutDate, p_roomType,p_price);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteBookingAfterCheckOut` (IN `p_checkOutDate` DATE)   BEGIN
    DELETE FROM RoomBooking
    WHERE CheckOutDate < p_checkOutDate;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `Customer_Name` varchar(50) NOT NULL,
  `Phone_no` varchar(50) NOT NULL,
  `Email_id` varchar(50) NOT NULL,
  `Address` varchar(50) NOT NULL,
  `CustomerId` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`Customer_Name`, `Phone_no`, `Email_id`, `Address`, `CustomerId`) VALUES
('ZeelG', '1234567895', 'aaa@gmail.com', 'aaa', 1),
('GajeraZ', '7894561230', 'bbb@gmail.com', 'bbb', 2),
('ZeelGajera', '1478523698', 'abc@gmail.com', 'asdf', 3);

-- --------------------------------------------------------

--
-- Table structure for table `roombooking`
--

CREATE TABLE `roombooking` (
  `roomNo` int(10) NOT NULL,
  `CustomerId` int(10) NOT NULL,
  `CheckInDate` date NOT NULL,
  `CheckOutDate` date NOT NULL,
  `RoomType` varchar(50) NOT NULL,
  `Price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`CustomerId`);

--
-- Indexes for table `roombooking`
--
ALTER TABLE `roombooking`
  ADD PRIMARY KEY (`roomNo`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `CustomerId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `roombooking`
--
ALTER TABLE `roombooking`
  MODIFY `roomNo` int(10) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
