create DATABASE if not exists vekselyator CHARACTER SET utf8 COLLATE utf8_general_ci;
use vekselyator;
create table if not exists Users            
            (ID bigint unique
            ,Brief varchar(50)
            ,PRIMARY KEY (ID)
            );
            

create table if not exists Chats
            (ID bigint unique
            ,PRIMARY KEY (ID));

create table if not exists UserChatRelation
            (ID bigint AUTO_INCREMENT
            ,ChatID bigint
            ,UserID bigint
            ,PRIMARY KEY (ID)
            ,FOREIGN KEY (ChatID) references Chats(ID)
            ,FOREIGN KEY (UserID) references Users(ID));
            
create table if not exists Operations
            (ID     bigint AUTO_INCREMENT
            ,UFrom  bigint
            ,UTo    bigint
            ,Qty    NUMERIC(20, 2)
            ,Date   datetime
            ,Comment varchar(255)
            ,PRIMARY KEY (ID)
            ,FOREIGN KEY (UFrom)  references  UserChatRelation(ID)
            ,FOREIGN KEY (UTo)    references  UserChatRelation(ID)
            );
commit;
