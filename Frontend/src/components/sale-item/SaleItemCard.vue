 <script setup>
import ListModel from '@/components/model/ListModel.vue'

defineProps({
    saleItems: {
        type: Array,
        required: true
    },
    view: {
        type: String,
        required: true
    },
    images: Array
})
</script>
 
<template>
    <ListModel :items="saleItems" :images="images" view="gallery">
        <template #saleItem="slotProps">
            <p class="itbms-brand text-[#A4A4A3] font-light text-[0.5rem] md:text-[0.6rem] lg:text-[0.7rem] ">{{ slotProps.itemInList.brandName }}</p>
            <div class="font-bold mb-3 text-[0.7rem] md:text-[0.7rem] lg:text-[0.9rem] text-[#332A1E]">
                <p class="itbms-model">{{ slotProps.itemInList.model }}</p>                
                <p>
                    <span class="itbms-ramGb ">{{ slotProps.itemInList.ramGb ?? '-' }}</span>/<span class="itbms-storageGb">{{ slotProps.itemInList.storageGb ?? '-' }}</span>
                    <span class="itbms-storageGb-unit">GB </span>
                    <span class="itbms-color">{{ slotProps.itemInList.color }}</span>
                </p>
            </div>
            <p class="text-[#6F879C] text-[0.7rem] md:text-[0.7rem] lg:text-[0.9rem]">
                <span class="itbms-price-unit">Baht</span> <span class="itbms-price">{{ slotProps.itemInList.price.toLocaleString() }}</span>
            </p>
        </template>
    </ListModel>
</template>
 
<style scoped>

</style> 