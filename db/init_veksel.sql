create DATABASE if not exists vekselyator CHARACTER SET utf8 COLLATE utf8_general_ci;
use vekselyator;
create table if not exists Users            
            (ID NUMERIC(30, 0)  unique primary key 
            ,Brief varchar(50)
            );
            

create table if not exists Chats
            (ID NUMERIC(30, 0) unique primary key);

create table if not exists UserChatRelation
            (ID int primary key AUTO_INCREMENT
            ,ChatID NUMERIC(30, 0) 
            ,UserID NUMERIC(30, 0)
            ,FOREIGN KEY (ChatID) references Chats(ID)
            ,FOREIGN KEY (UserID) references Users(ID));
            
create table if not exists Operations
            (ID     int primary key AUTO_INCREMENT
            ,UFrom  NUMERIC(30, 0) 
            ,UTo    NUMERIC(30, 0)
            ,Qty    NUMERIC(20, 2)
            ,ChatID NUMERIC(30, 0)
            ,Date   datetime
            ,Comment varchar(255)
            ,FOREIGN KEY (UFrom)  references  Users(ID)
            ,FOREIGN KEY (UTo)    references  Users(ID)
            ,FOREIGN KEY (ChatID) references  Chats(ID)
            );
commit;
