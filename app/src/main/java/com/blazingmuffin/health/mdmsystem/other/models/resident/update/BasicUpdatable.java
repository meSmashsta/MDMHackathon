package com.blazingmuffin.health.mdmsystem.other.models.resident.update;

import com.blazingmuffin.health.mdmsystem.other.interfaces.IUpdatable;
import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;

/**
 * Created by lenovo on 10/21/2017.
 */

public class BasicUpdatable implements IUpdatable<ResidentEntity> {
    @Override
    public ResidentEntity update(ResidentEntity residentEntity) {
        return residentEntity;
    }
}
