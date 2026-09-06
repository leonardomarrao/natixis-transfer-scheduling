INSERT INTO tb_transfer (source_account, destination_account, amount, fee, scheduled_date, transfer_date) 
VALUES ('123456', '654321', 1000.00, 12.00, CURRENT_DATE, CURRENT_DATE);

INSERT INTO tb_transfer (source_account, destination_account, amount, fee, scheduled_date, transfer_date) 
VALUES ('987654', '456789', 5000.00, 60.00, CURRENT_DATE, CURRENT_DATE + 5);