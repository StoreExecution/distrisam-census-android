package com.storexecution.cocacola.util;

import com.storexecution.cocacola.model.ValidationConditon;

import io.realm.DynamicRealm;
import io.realm.RealmList;
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

        if (!schema.get("Salepoint").hasField("facades"))
            schema.get("Salepoint")
                    .addField("facades", int.class);
        if (!schema.get("Salepoint").hasField("rtmId"))
            schema.get("Salepoint")
                    .addField("rtmId", int.class);

        if (!schema.get("Salepoint").hasField("notificationId"))
            schema.get("Salepoint")
                    .addField("notificationId", int.class);

        if (!schema.contains("Sector")) {
            schema.create("Sector")
                    .addField("id", int.class)
                    .addPrimaryKey("id")
                    .addField("name", String.class)
                    .addField("fileLink", String.class)
                    .addField("localFile", String.class)
                    .addField("day", String.class)
                    .addField("position", int.class)
                    .addField("open", int.class)
                    .addField("deleted", int.class)
                    .addField("user_id", int.class);


        }

        if (!schema.contains("RTMSalepoint")) {
            schema.create("RTMSalepoint")
                    .addField("id", int.class)
                    .addPrimaryKey("id")
                    .addField("salepoint_name", String.class)
                    .addField("owner_name", String.class)
                    .addField("owner_phone", String.class)
                    .addField("affected_date", String.class)
                    .addField("adress", String.class)
                    .addField("wilaya_id", int.class)
                    .addField("commune", String.class)
                    .addField("image", String.class)
                    .addField("latitude", double.class)
                    .addField("longitude", double.class)
                    .addField("affected_user_id", int.class);


        }

        if (!schema.get("RTMSalepoint").hasField("order"))
            schema.get("RTMSalepoint")
                    .addField("order", int.class);


        if (!schema.contains("ActivityChange")) {
            schema.create("ActivityChange")
                    .addField("mobileId", String.class)
                    .addPrimaryKey("mobileId")
                    .addField("comment", String.class)
                    .addField("mobileDate", String.class)

                    .addField("latitude", double.class)
                    .addField("longitude", double.class)
                    .addField("user_id", int.class)
                    .addField("type", int.class)
                    .addField("rtmId", int.class);


        }


        if (!schema.get("ActivityChange").hasField("synced"))
            schema.get("ActivityChange")
                    .addField("synced", boolean.class);

        if (!schema.get("RTMSalepoint").hasField("done"))
            schema.get("RTMSalepoint")
                    .addField("done", int.class);


        if (!schema.contains("UserTrack")) {
            schema.create("UserTrack")
                    .addField("userId", int.class)
                    .addPrimaryKey("userId")
                    .addField("latitude", double.class)
                    .addField("longitude", double.class)
                    .addField("time", String.class)
                    .addField("posAccept", int.class)
                    .addField("posRefuse", int.class)
                    .addField("posClosed", int.class);

        }

        if (!schema.get("UserTrack").hasField("wilayaId"))
            schema.get("UserTrack")
                    .addField("wilayaId", int.class);
        if (!schema.get("UserTrack").hasField("username"))
            schema.get("UserTrack")
                    .addField("username", String.class);

        if (!schema.contains("ValidationConditon")) {
            schema.create("ValidationConditon")
                    .addField("id", int.class)
                    .addPrimaryKey("id")
                    .addField("salepointValidationId", int.class)
                    .addField("dataId", String.class)
                    .addField("dataType", String.class)
                    .addField("validationConditionId", int.class)
                    .addField("status", int.class);


        }
        if (!schema.contains("Notification")) {
            schema.create("Notification")
                    .addField("id", int.class)
                    .addPrimaryKey("id")
                    .addField("salepointId", int.class)
                    .addField("mobileId", String.class)
                    .addField("comment", String.class)
                    .addField("userId", int.class)
                    .addRealmListField("conditions", schema.get("ValidationConditon"))
                    .addField("status", int.class);


        }

        if (schema.get("Notification").hasField("conditons")) {

            schema.get("Notification").removeField("conditons");
        }

        if (!schema.get("Notification").hasField("conditions")) {
            schema.get("Notification").addRealmListField("conditions", schema.get("ValidationConditon"));

        }


        if (!schema.get("Salepoint").hasField("user_id"))
            schema.get("Salepoint").addField("user_id", int.class);

        if (!schema.get("Photo").hasField("user_id"))
            schema.get("Photo").addField("user_id", int.class);


        if (!schema.get("Photo").hasField("error"))
            schema.get("Photo").addField("error", boolean.class);

        if (!schema.get("Salepoint").hasField("error"))
            schema.get("Salepoint").addField("error", boolean.class);
    }
}
