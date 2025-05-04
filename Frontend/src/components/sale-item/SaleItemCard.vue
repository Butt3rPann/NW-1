<script setup>
import ListModel from '../model/ListModel.vue';

defineProps({
    saleItems: {
        type: Array,
        required: true
    },
    view: {
        type: String,
        required: true
    }
})
</script>
 
<template>
    <ListModel :items="saleItems" :view="view">
        <template #saleItem="slotProps">
            <div class="p-[0.8vw] w-fit">
                <p class="itbms-brand text-[#A4A4A3] font-light text-[0.8vw] ">{{ slotProps.itemInList.brandName }}</p>
                <div class="font-bold mb-[1vw] text-[1vw] text-[#332A1E]">
                    <p class="itbms-model">{{ slotProps.itemInList.model }}</p>                
                    <p>
                        <span class="itbms-ramGb ">{{ slotProps.itemInList.ramGb ?? '-' }}</span>/<span class="itbms-storageGb">{{ slotProps.itemInList.storageGb ?? '-' }}</span>
                        <span class="itbms-storageGb-unit">GB</span>
                    </p>
                </div>
                <p class=" text-[#6F879C] text-[1vw]">
                    <span class="itbms-price-unit">Baht</span> <span class="itbms-price">{{ slotProps.itemInList.price.toLocaleString() }}</span>
                </p>
            </div>
        </template>
    </ListModel>
</template>
 
<style scoped>

</style>