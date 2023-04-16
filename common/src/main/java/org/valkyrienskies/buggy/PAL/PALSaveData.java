package org.valkyrienskies.buggy.PAL;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.commons.lang3.SerializationUtils;


public class PALSaveData extends SavedData {

    public static String SAVED_DATA_ID = "buggy_data";
    public static PALNetwork network;
    public void setNetwork(PALNetwork newN){
        network = newN;
    }

    public static PALSaveData createEmpty() {
        return new PALSaveData(); // .apply
    }

    public static PALSaveData load(CompoundTag compoundTag) {
        PALSaveData saveData = new PALSaveData();
        if(network != null){

            byte[] dataAsBytes = compoundTag.getByteArray(SAVED_DATA_ID);
            PALNetworkData data = SerializationUtils.deserialize(dataAsBytes);

            network.loadData(data);

//        // Read bytes from the [CompoundTag]
//        byte[] queryableShipDataAsBytes = compoundTag.getByteArray(QUERYABLE_SHIP_DATA_NBT_KEY);
//        byte[] chunkAllocatorAsBytes = compoundTag.getByteArray(CHUNK_ALLOCATOR_NBT_KEY);
//        byte[] pipelineAsBytes = compoundTag.getByteArray(PIPELINE_NBT_KEY);


        }
        return saveData;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        if(network != null){
            byte[] data = SerializationUtils.serialize(network.getData().getClass());

            compoundTag.putByteArray(SAVED_DATA_ID, data);

            //compoundTag.putByteArray(PIPELINE_NBT_KEY, vsCore.serializePipeline(pipeline))


        }
        return compoundTag;
    }

    /**
     * This is not efficient, but it will work for now.
     */
    @Override
    public boolean isDirty(){
        return true;
    }
}
        