create table accounts (
                         id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(50) NOT NULL,
                         email VARCHAR(20) NOT NULL,
                         password VARCHAR(20) NOT NULL,
                         roles VARCHAR(20) NOT NULL
);