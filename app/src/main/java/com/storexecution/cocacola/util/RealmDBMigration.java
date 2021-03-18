package com.storexecution.cocacola.util;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class RealmDBMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();


        if (!schema.get("Salepoint").hasField("smileyRating"))
            schema.get("Salepoint")
                    .addField("smileyRating", int.class);


        if (!schema.get("Salepoint").hasField("classification"))
            schema.get("Salepoint")
                    .addField("classification", int.class);

        if (!schema.get("Salepoint").hasField("closed"))
            schema.get("Salepoint")
                    .addField("closed", int.class);

        if (!schema.get("Salepoint").hasField("closedReason"))
            schema.get("Salepoint")
                    .addField("closedReason", int.class);

        if (!schema.get("TagElement").hasField("id"))
            schema.get("TagElement")
                    .addField("id", String.class);

        if (!schema.get("ConcurrentFridge").hasField("mobile_id"))
            schema.get("ConcurrentFridge")
                    .addField("mobile_id", String.class);

        if (!schema.get("Salepoint").hasField("otherRefuseReason"))
            schema.get("Salepoint")
                    .addField("otherRefuseReason", String.class);

        if (!schema.get("Salepoint").hasField("refuseReason"))
            schema.get("Salepoint")
                    .addField("refuseReason", int.class);

        if (!schema.get("Salepoint").hasField("refuse"))
            schema.get("Salepoint")
                    .addField("refuse", int.class);




        if (!schema.get("Salepoint").hasField("purchaseSodaHigh"))
            schema.get("Salepoint")
                    .addField("purchaseSodaHigh", String.class);


        if (!schema.get("Salepoint").hasField("purchaseSodaLow"))
            schema.get("Salepoint")
                    .addField("purchaseSodaLow", String.class);

        if (!schema.get("Salepoint").hasField("posSystem"))
            schema.get("Salepoint")
                    .addField("posSystem", int.class);




    }
}
