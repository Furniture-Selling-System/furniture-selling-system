CREATE TABLE customer
(
    id      int(5) PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name    varchar(50) NOT NULL UNIQUE,
    address text        NOT NULL,
    phone   varchar(11) NOT NULL
);

CREATE TABLE sale_order
(
    fk_customer_id      int(5)      NOT NULL,
    id                  int(5)      PRIMARY KEY AUTO_INCREMENT NOT NULL,
    c_address           text        NOT NULL,
    c_name              varchar(50) NOT NULL,
    cost_total          int(7)      NOT NULL CHECK (cost_total > 0),
    furniture_status    int(1)      NOT NULL DEFAULT 0 CHECK (furniture_status IN (0, 1, 2, 3, 8, 9)),
    create_date    date             NOT NULL DEFAULT CURRENT_DATE,
    FOREIGN KEY (fk_customer_id) REFERENCES customer (id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
);

CREATE TABLE furniture
(
    id          int(5) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    cost        int(5) NOT NULL CHECK (cost > 0),
    name        varchar(50) NOT NULL
);

CREATE TABLE sale_order_list
(
    fk_sale_order_id             int(5) NOT NULL,
    fk_furniture_id         int(5) NOT NULL,
    PRIMARY KEY (fk_sale_order_id, fk_furniture_id),
    quantity                int(2) NOT NULL CHECK (quantity > 0),
    cost_withholding        int(5) NOT NULL CHECK (cost_withholding > 0),
    FOREIGN KEY (fk_sale_order_id) REFERENCES sale_order (id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION,
    FOREIGN KEY (fk_furniture_id) REFERENCES furniture (id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
);

CREATE TABLE material
(
    id          int(5)      PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name        varchar(50) NOT NULL,
    quantity    int(4)      NOT NULL CHECK (quantity >= 0),
    minimum     int(3)      NOT NULL CHECK (minimum >= 0)
);

CREATE TABLE bill_of_material
(
    fk_furniture_id   int(5) NOT NULL,
    fk_material_id  int(5) NOT NULL,
    PRIMARY KEY (fk_furniture_id, fk_material_id),
    spend           int(3) NOT NULL CHECK (spend > 0),
    FOREIGN KEY (fk_furniture_id) REFERENCES furniture (id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION,
    FOREIGN KEY (fk_material_id) REFERENCES material (id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
);
