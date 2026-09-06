INSERT INTO tb_transfers
(source_account, destination_account, amount, fee, scheduled_date, transfer_date)
VALUES
('123456', '654321', 1000.00, 33.00, CURRENT_DATE, CURRENT_DATE);


INSERT INTO tb_transfers
(source_account, destination_account, amount, fee, scheduled_date, transfer_date)
VALUES
('111111', '222222', 1500.00, 135.00, CURRENT_DATE, DATEADD('DAY', 5, CURRENT_DATE));


INSERT INTO tb_transfers
(source_account, destination_account, amount, fee, scheduled_date, transfer_date)
VALUES
('333333', '444444', 3000.00, 246.00, CURRENT_DATE, DATEADD('DAY', 15, CURRENT_DATE));


INSERT INTO tb_transfers
(source_account, destination_account, amount, fee, scheduled_date, transfer_date)
VALUES
('555555', '666666', 3000.00, 207.00, CURRENT_DATE, DATEADD('DAY', 25, CURRENT_DATE));


INSERT INTO tb_transfers
(source_account, destination_account, amount, fee, scheduled_date, transfer_date)
VALUES
('777777', '888888', 3000.00, 141.00, CURRENT_DATE, DATEADD('DAY', 35, CURRENT_DATE));


INSERT INTO tb_transfers
(source_account, destination_account, amount, fee, scheduled_date, transfer_date)
VALUES
('999999', '123123', 3000.00, 51.00, CURRENT_DATE, DATEADD('DAY', 45, CURRENT_DATE));