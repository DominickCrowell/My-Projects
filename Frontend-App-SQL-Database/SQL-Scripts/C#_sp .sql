-- Programmer: Jacob Mitchell 
/*
Populate Sprocs
*/
--People 
DROP PROCEDURE IF EXISTS sp_GetPeopleData
GO
CREATE PROCEDURE sp_GetPeopleData
AS
BEGIN
    SELECT * FROM People;
END
GO

-- Officers 
DROP PROCEDURE IF EXISTS sp_GetOfficersData
GO
CREATE PROCEDURE sp_GetOfficersData
AS 
BEGIN 
	SELECT * 
	FROM Officers;
END;
GO

-- Events 
DROP PROCEDURE IF EXISTS sp_GetEventsData
GO
CREATE PROCEDURE sp_GetEventsData
AS
BEGIN
    SELECT *
    FROM Events;
END;
GO

-- Membership Records 

DROP PROCEDURE IF EXISTS sp_GetMembershipRecordsData
GO
CREATE PROCEDURE sp_GetMembershipRecordsData
AS
BEGIN
    SELECT *
    FROM MembershipRecords;
END;
GO

-- Locations 
DROP PROCEDURE IF EXISTS sp_GetLocationsData
GO
CREATE PROCEDURE sp_GetLocationsData
AS
BEGIN
    SELECT *
    FROM Locations;
END;
GO
-- Expendetures 
DROP PROCEDURE IF EXISTS sp_GetExpendeturesData
GO
CREATE PROCEDURE sp_GetExpendeturesData
AS
BEGIN
    SELECT *
    FROM Expeditures;
END;
GO
-- Recievables 
DROP PROCEDURE IF EXISTS sp_GetRecievablesData
GO
CREATE PROCEDURE sp_GetRecievablesData
AS
BEGIN
    SELECT *
    FROM Recievables;
END;
GO

-- Academic Years Data
DROP PROCEDURE IF EXISTS sp_GetAcademicYearsData
GO 
CREATE PROCEDURE sp_GetAcademicYearsData
AS 
BEGIN 
	SELECT * 
	FROM vw_AcademicYears;
END;
GO 	

EXEC sp_GetAcademicYearsData


/* 
Insert Sprocs
*/

-- People
DROP PROCEDURE IF EXISTS sp_InsertPerson
GO
CREATE PROCEDURE sp_InsertPerson
    @LName VARCHAR(50),
    @FName VARCHAR(50),
    @Email VARCHAR(50),
    @ACMID CHAR(7),
    @Classification VARCHAR(50),
    @StudentVIPID VARCHAR(50),
    @PaymentDate DATE,
    @Organization VARCHAR(50)
AS
BEGIN
    INSERT INTO [People] ([LName], [FName], [Email], [ACMID], [Classification], [StudentVIPID], [PaymentDate], [Organization])
    VALUES (@LName, @FName, @Email, @ACMID, @Classification, @StudentVIPID, @PaymentDate, @Organization);
END
GO

/* Testing
EXEC sp_InsertPerson 'Simpson', 'Homer', 'email', '234', 'freeloader', NULL, NULL, NULL;
EXEC sp_InsertPerson 'Mitchell', 'Jacob', 'some thingy', '999', 'not a freeloader', NULL, NULL, NULL;
select * from People
*/ 


-- Officers
DROP PROCEDURE IF EXISTS sp_InsertOfficer
GO
CREATE PROCEDURE sp_InsertOfficer
(
    @PeopleID INT,
    @Position VARCHAR(50),
    @StartDate DATE,
    @EndDate DATE
)
AS
BEGIN
    INSERT INTO Officers (PeopleID, Position, StartDate, EndDate)
    VALUES (@PeopleID, @Position, @StartDate, @EndDate);
END;
GO

-- Locations
DROP PROCEDURE IF EXISTS sp_InsertLocation
GO
CREATE PROCEDURE sp_InsertLocation
(
    @Name VARCHAR(50),
    @Description VARCHAR(500),
    @Building VARCHAR(50),
    @Room VARCHAR(50),
    @Address1 VARCHAR(50),
    @Address2 VARCHAR(50),
    @City VARCHAR(50),
    @State VARCHAR(50),
    @ZipCode CHAR(10)
)
AS
BEGIN
    INSERT INTO Locations (Name, Description, Building, Room, Address1, Address2, City, State, ZipCode)
    VALUES (@Name, @Description, @Building, @Room, @Address1, @Address2, @City, @State, @ZipCode);
END;
GO

-- Events 
DROP PROCEDURE IF EXISTS sp_InsertEventInformation
GO
CREATE PROCEDURE sp_InsertEventInformation
    @NewOfficerID INT,
    @NewEventName VARCHAR(50),
    @NewDescription VARCHAR(500),
    @NewEventStartTime DATETIME,
    @NewEventEndTime DATETIME,
    @NewLocationID INT
AS
BEGIN
    INSERT INTO [Events] ([OfficerID], [EventName], [Description], [EventStartTime], [EventEndTime], [LocationID])
    VALUES (@NewOfficerID, @NewEventName, @NewDescription, @NewEventStartTime, @NewEventEndTime, @NewLocationID);
END
GO

/* Testing
EXEC sp_InsertEventInformation NULL, 'LegoLand', 'fun thing', '3/2/23', '3/2/23', NULL;
select * from Events
*/



/*
Update Sprocs
*/
-- Person
DROP PROCEDURE IF EXISTS sp_UpdatePersonByID
GO
CREATE PROCEDURE sp_UpdatePersonByID
    @PeopleID INT, 
    @NewLName VARCHAR(50),
    @NewFName VARCHAR(50),
	@NewEmail VARCHAR(50),
    @NewACMID CHAR(7),
    @NewClassification VARCHAR(50),
    @NewStudentVIPID VARCHAR(50),
    @NewPaymentDate DATE,
    @NewOrganization VARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;

    -- Check if the person with the given ID exists
    IF EXISTS (SELECT 1 FROM [People] WHERE [PeopleID] = @PeopleID) -- Replace 'ID' with your actual column name
    BEGIN
        -- Update the person's record
        UPDATE [People]
        SET [LName] = @NewLName,
            [FName] = @NewFName,
			[Email] = @NewEmail,
            [ACMID] = @NewACMID,
            [Classification] = @NewClassification,
            [StudentVIPID] = @NewStudentVIPID,
            [PaymentDate] = @NewPaymentDate,
            [Organization] = @NewOrganization
        WHERE [PeopleID] = @PeopleID; -- Replace 'ID' with your actual column name
    END
    ELSE
    BEGIN
        -- Optionally, you can raise an error or handle the case where the person doesn't exist
        RAISERROR ('Person with provided ID does not exist.', 16, 1);
        -- Or you can simply do nothing
        -- SELECT 'Person with provided ID does not exist.';
    END
END

GO

-- Officers 
DROP PROCEDURE IF EXISTS sp_UpdateOfficerByID
GO
CREATE PROCEDURE sp_UpdateOfficerByID
    @OfficerID INT,
    @NewPeopleID INT,
    @NewPosition VARCHAR(50),
    @NewStartDate DATE,
    @NewEndDate DATE
AS
BEGIN
    SET NOCOUNT ON;

    -- Check if the officer with the given ID exists
    IF EXISTS (SELECT 1 FROM [Officers] WHERE [OfficerID] = @OfficerID)
    BEGIN
        -- Update the officer's record
        UPDATE [Officers]
        SET [PeopleID] = @NewPeopleID,
            [Position] = @NewPosition,
            [StartDate] = @NewStartDate,
            [EndDate] = @NewEndDate
        WHERE [OfficerID] = @OfficerID;
    END
    ELSE
    BEGIN
        -- Optionally, you can raise an error or handle the case where the officer doesn't exist
        RAISERROR ('Officer with provided ID does not exist.', 16, 1);
        -- Or you can simply do nothing
        -- SELECT 'Officer with provided ID does not exist.';
    END
END

GO

-- Events 
DROP PROCEDURE IF EXISTS sp_UpdateEventByID
GO 
CREATE PROCEDURE sp_UpdateEventByID
    @EventID INT,
    @NewOfficerID INT,
    @NewEventName VARCHAR(50),
    @NewDescription VARCHAR(500),
    @NewEventStartTime DATETIME,
    @NewEventEndTime DATETIME,
    @NewLocationID INT
AS
BEGIN
    SET NOCOUNT ON;

    -- Check if the event with the given ID exists
    IF EXISTS (SELECT 1 FROM [Events] WHERE [EventID] = @EventID)
    BEGIN
        -- Update the event's record
        UPDATE [Events]
        SET [OfficerID] = @NewOfficerID,
            [EventName] = @NewEventName,
            [Description] = @NewDescription,
            [EventStartTime] = @NewEventStartTime,
            [EventEndTime] = @NewEventEndTime,
            [LocationID] = @NewLocationID
        WHERE [EventID] = @EventID;
    END
    ELSE
    BEGIN
        -- Optionally, you can raise an error or handle the case where the event doesn't exist
        RAISERROR ('Event with provided ID does not exist.', 16, 1);
        -- Or you can simply do nothing
        -- SELECT 'Event with provided ID does not exist.';
    END
END
GO

-- Locations 
DROP PROCEDURE IF EXISTS sp_UpdateLocationByID
GO
CREATE PROCEDURE sp_UpdateLocationByID
    @LocationID INT,
    @NewName VARCHAR(50),
    @NewDescription VARCHAR(500),
    @NewBuilding VARCHAR(50),
    @NewRoom VARCHAR(50),
    @NewAddress1 VARCHAR(50),
    @NewAddress2 VARCHAR(50),
    @NewCity VARCHAR(50),
    @NewState VARCHAR(50),
    @NewZipCode CHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    -- Check if the location with the given ID exists
    IF EXISTS (SELECT 1 FROM [Locations] WHERE [LocationID] = @LocationID)
    BEGIN
        -- Update the location's record
        UPDATE [Locations]
        SET [Name] = @NewName,
            [Description] = @NewDescription,
            [Building] = @NewBuilding,
            [Room] = @NewRoom,
            [Address1] = @NewAddress1,
            [Address2] = @NewAddress2,
            [City] = @NewCity,
            [State] = @NewState,
            [ZipCode] = @NewZipCode
        WHERE [LocationID] = @LocationID;
    END
    ELSE
    BEGIN
        -- Optionally, you can raise an error or handle the case where the location doesn't exist
        RAISERROR ('Location with provided ID does not exist.', 16, 1);
        -- Or you can simply do nothing
        -- SELECT 'Location with provided ID does not exist.';
    END
END

GO

-- Membership Records
DROP PROCEDURE IF EXISTS sp_UpdateMembershipRecordByID
GO
CREATE PROCEDURE sp_UpdateMembershipRecordByID
    @MRecordID INT,
    @NewPeopleID INT,
    @NewStartDate DATE,
    @NewEndDate DATE
AS
BEGIN
    SET NOCOUNT ON;

    -- Check if the membership record with the given ID exists
    IF EXISTS (SELECT 1 FROM [MembershipRecords] WHERE [MRecordID] = @MRecordID)
    BEGIN
        -- Update the membership record
        UPDATE [MembershipRecords]
        SET [PeopleID] = @NewPeopleID,
            [StartDate] = @NewStartDate,
            [EndDate] = @NewEndDate
        WHERE [MRecordID] = @MRecordID;
    END
    ELSE
    BEGIN
        -- Optionally, you can raise an error or handle the case where the membership record doesn't exist
        RAISERROR ('Membership record with provided ID does not exist.', 16, 1);
        -- Or you can simply do nothing
        -- SELECT 'Membership record with provided ID does not exist.';
    END
END

GO

/*
Delete SProcs (Just in case!)
*/

-- Stored procedure to automatically edit/delete a person's information

DROP PROCEDURE IF EXISTS sp_DeletePerson
GO

-- delete a person
CREATE PROCEDURE sp_DeletePerson
    @Email VARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;

    -- Check if person with the given email exists
    IF EXISTS (SELECT 1 FROM [People] WHERE [Email] = @Email)
    BEGIN
        -- Delete the person's record
        DELETE FROM [People]
        WHERE [Email] = @Email;
    END
    ELSE
    BEGIN
        -- Optionally, you can raise an error or handle the case where the person doesn't exist
        RAISERROR ('Person with provided email does not exist.', 16, 1);
        -- Or you can simply do nothing
        -- SELECT 'Person with provided email does not exist.';
    END
END

GO

/*
Etc SProcs
*/

-- Programmer: Jacob Mitchell 

DROP PROCEDURE IF EXISTS sp_EventAttendance;
GO
CREATE PROCEDURE sp_EventAttendance 
    @StartDate DATE,
    @EndDate DATE
AS
BEGIN
    SELECT 
        ea.EventID,
        e.EventName AS [Event Name],
        e.EventStartTime AS [Start Date],
        e.EventEndTime AS [End Date],
        COUNT(ea.PeopleID) AS [Total Attendees],
        SUM(CASE WHEN p.ACMID IS NOT NULL THEN 1 ELSE 0 END) AS [Member Attendees],
        SUM(CASE WHEN p.ACMID IS NULL THEN 1 ELSE 0 END) AS [Non-Member Attendees],
        CASE 
            WHEN COUNT(ea.PeopleID) = 0 THEN '0%'
            ELSE CONCAT(CAST(ROUND((SUM(CASE WHEN p.ACMID IS NOT NULL THEN 1 ELSE 0 END) * 100.0 / COUNT(ea.PeopleID)), 0) AS INT), '%') 
        END AS [Percentage Members],
        CASE 
            WHEN COUNT(ea.PeopleID) = 0 THEN '0%'
            ELSE CONCAT(100 - CAST(ROUND((SUM(CASE WHEN p.ACMID IS NOT NULL THEN 1 ELSE 0 END) * 100.0 / COUNT(ea.PeopleID)), 0) AS INT), '%') 
        END AS [Percentage Non-Members]
    FROM 
        Events e
    LEFT JOIN 
        EventAttendees ea ON e.EventID = ea.EventID 
    LEFT JOIN 
        People p ON ea.PeopleID = p.PeopleID
    WHERE 
        ea.EventID IS NOT NULL
        AND e.EventStartTime >= @StartDate
        AND e.EventStartTime <= @EndDate
    GROUP BY 
        e.EventName, 
        ea.EventID,
        e.EventStartTime,
        e.EventEndTime
    ORDER BY 
        ea.EventID ASC;
END;

-- Testing:
--EXEC sp_EventAttendance @StartDate = '2018-08-14', @EndDate = '2019-08-14';
--EXEC sp_EventAttendance @StartDate = '2019-08-16', @EndDate = '2020-08-14';
--EXEC sp_EventAttendance @StartDate = '2020-08-15', @EndDate = '2021-08-14';
--EXEC sp_EventAttendance @StartDate = '2021-08-21', @EndDate = '2022-08-14';
--EXEC sp_EventAttendance @StartDate = '2022-08-21', @EndDate = '2023-08-14';
--EXEC sp_EventAttendance @StartDate = '2023-08-21', @EndDate = '2024-08-14

GO


-- Programmer: Jacob Mitchell 

-- A stored procedure that associates a person with a certain event. 
-- Note: This procedure will need to be modified later to be able to function in 
--       the front end interface. 
DROP PROCEDURE IF EXISTS sp_AssociatePeopleWithEventAttendance;
GO

CREATE PROCEDURE sp_AssociatePeopleWithEventAttendance
    @EventID INT,
    @PeopleID INT
AS
BEGIN
    IF NOT EXISTS 
	(SELECT 1 
	FROM EventAttendees 
	WHERE EventID = @EventID AND PeopleID = @PeopleID)
    BEGIN
        INSERT INTO EventAttendees 
		(EventID, PeopleID)
        VALUES 
		(@EventID, @PeopleID);
    END;
	ELSE
        THROW 52000, 'Error: This person is already registered for this event.', 1;
END;

-- Testing 
-- EXEC sp_AssociatePeopleWithEventAttendance @EventID = 1, @PeopleID = 204;
--select * from EventAttendees WHERE EventID = '1'

GO

-- Yearly Member Report List (Lists a the total members for each academic year) 
DROP PROCEDURE IF EXISTS sp_YearlyMemberReport;
GO

CREATE PROCEDURE sp_YearlyMemberReport
    @AcademicYear VARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @StartDate DATE, @EndDate DATE;

    SET @StartDate = CONCAT('20', SUBSTRING(@AcademicYear, 1, 2), '-09-01');
    SET @EndDate = CONCAT('20', SUBSTRING(@AcademicYear, 4, 2), '-08-31');

    SELECT 
        p.*, 
        mr.*
    FROM 
        People p
    INNER JOIN 
        MembershipRecords mr ON p.PeopleID = mr.PeopleID
    INNER JOIN 
        vw_AcademicYears ay ON mr.StartDate >= ay.TermStartDate AND mr.StartDate <= ay.TermEndDate
    WHERE 
        mr.StartDate >= @StartDate AND mr.StartDate <= @EndDate;
END;

/* Testing
EXEC sp_YearlyMemberReport @AcademicYear = '19-20';
EXEC sp_YearlyMemberReport @AcademicYear = '20-21';
EXEC sp_YearlyMemberReport @AcademicYear = '21-22';
EXEC sp_YearlyMemberReport @AcademicYear = '22-23';
EXEC sp_YearlyMemberReport @AcademicYear = '23-24';
*/


-- Yearly Financial Report for expenditures 

-- Expeditures SP?

-- Summed Expenses 
select 
e.ExpenditureID,
LineItemDescription,
--SUM(UnitPrice) AS SummedUnitPrice,
LineItemCategory
from [dbo].[ExpenditureLineItems] eli
inner join [dbo].[Expenditures] e
on eli.ExpenditureID = e.ExpenditureID
inner join AccountingAccounts a
on a.AccountingID = e.AccountingID
/*
GROUP BY 
e.ExpenditureID,
LineItemDescription,
LineItemCategory
*/

--select * from ExpenditureLineItems












