Q1.1
    {
        SELECT c.id,c.name,c.address,c.phone FROM customer AS c;
    }
Q1.2
    {
        SELECT f.id,f.name,f.cost FROM furniture AS f;
    }
Q2.1
    {
        SELECT so.id,f.id FROM sale_order_list AS ol
            INNER JOIN sale_order AS so
                ON so.id = ol.fk_sale_order_id
            INNER JOIN furniture AS f
                ON f.id = ol.fk_furniture_id
        WHERE so.furniture_status=0

    }

