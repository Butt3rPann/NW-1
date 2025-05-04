<script setup>
import ListModel from '@/components/model/ListModel.vue';
import { getSaleItems } from '@/libs/fetchUtils';
import { onMounted, ref } from 'vue';

const saleItems = ref([])

onMounted(async () => {
    try {
        saleItems.value = await getSaleItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`)
    } catch (error) {
        console.log(error);
    }
})
</script>
 
<template>
    <div class="font-rubik mx-[10vw] my-[4vw] space-y-[2vw] mt-[7vw]">
        <p class="text-[4vw] font-bold text-[#332A1E]">ITB MShop</p>
        <div class="flex justify-between items-center">
            <div class="relative flex items-center">
                <input type="text" name="search" placeholder="Search..." class="shadow-[0_0.03vw_0.2vw_0_rgba(0,0,0,0.15)] rounded-[0.4vw] py-[0.6vw] px-[1.5vw] w-[25vw] placeholder:text-[0.9vw] placeholder:text-[#6F879C] placeholder:font-light text-[#332A1E] text-[0.9vw]">
                <span class=" absolute right-[1.5vw]">
                    <svg xmlns="http://www.w3.org/2000/svg" width="1.3vw" height="1.3vw" viewBox="0 0 24 24">
                    	<path fill="#6F879C" d="M9.5 16q-2.725 0-4.612-1.888T3 9.5t1.888-4.612T9.5 3t4.613 1.888T16 9.5q0 1.1-.35 2.075T14.7 13.3l5.6 5.6q.275.275.275.7t-.275.7t-.7.275t-.7-.275l-5.6-5.6q-.75.6-1.725.95T9.5 16m0-2q1.875 0 3.188-1.312T14 9.5t-1.312-3.187T9.5 5T6.313 6.313T5 9.5t1.313 3.188T9.5 14" />
                    </svg>
                </span>
            </div>
            <div class="flex space-x-[1.5vw]">
                <div class="shadow-[0_0.03vw_0.2vw_0_rgba(0,0,0,0.15)] rounded-[0.4vw] py-[0.6vw] px-[1.5vw] w-fit">
                    <p class="text-[0.9vw] text-[#ABBCC9] font-medium flex items-center space-x-[0.7vw]">
                        <span>Sort by :</span>
                        <span class="text-[#6F879C]">Oldest</span>  
                        <span>
                            <svg xmlns="http://www.w3.org/2000/svg" width="1.3vw" height="1.3vw" viewBox="0 0 24 24">
                            	<g fill="none" fill-rule="evenodd">
                            		<path d="M24 0v24H0V0zM12.593 23.258l-.011.002l-.071.035l-.02.004l-.014-.004l-.071-.035q-.016-.005-.024.005l-.004.01l-.017.428l.005.02l.01.013l.104.074l.015.004l.012-.004l.104-.074l.012-.016l.004-.017l-.017-.427q-.004-.016-.017-.018m.265-.113l-.013.002l-.185.093l-.01.01l-.003.011l.018.43l.005.012l.008.007l.201.093q.019.005.029-.008l.004-.014l-.034-.614q-.005-.019-.02-.022m-.715.002a.02.02 0 0 0-.027.006l-.006.014l-.034.614q.001.018.017.024l.015-.002l.201-.093l.01-.008l.004-.011l.017-.43l-.003-.012l-.01-.01z" />
                            		<path fill="#ABBCC9" d="M12.707 15.707a1 1 0 0 1-1.414 0L5.636 10.05A1 1 0 1 1 7.05 8.636l4.95 4.95l4.95-4.95a1 1 0 0 1 1.414 1.414z" />
                            	</g>
                            </svg>
                        </span>
                    </p>
                </div>
                <div class="shadow-[0_0.03vw_0.2vw_0_rgba(0,0,0,0.15)] rounded-[0.4vw] py-[0.6vw] px-[1vw] w-fit flex items-center space-x-[0.3vw]">
                    <span>
                        <svg xmlns="http://www.w3.org/2000/svg" width="1.3vw" height="1.3vw" viewBox="0 0 24 24">
                        	<g class="filter-outline">
                        		<path fill="#6F879C" fill-rule="evenodd" d="M3 7a1 1 0 0 1 1-1h16a1 1 0 1 1 0 2H4a1 1 0 0 1-1-1m2 4.5a1 1 0 0 1 1-1h12a1 1 0 1 1 0 2H6a1 1 0 0 1-1-1M8 16a1 1 0 0 1 1-1h6a1 1 0 1 1 0 2H9a1 1 0 0 1-1-1" class="Vector 38 (Stroke)" clip-rule="evenodd" />
                        	</g>
                        </svg>
                    </span>
                    <p class="text-[0.9vw] text-[#6F879C] font-medium">Filter</p>
                </div>
            </div>
        </div>
        <ListModel v-if="saleItems.length" :items="saleItems">
            <template #saleItem="slotProps">
                <div class="p-[0.8vw] w-fit">
                    <p class="itbms-brand text-[#A4A4A3] font-light text-[0.8vw] ">{{ slotProps.itemInList.brandName }}</p>
                    <div class="font-bold mb-[1vw] text-[1vw] text-[#332A1E]">
                        <p class="itbms-model hover:text-[#6F879C]">{{ slotProps.itemInList.model }}</p>             
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
        <div v-else class="flex flex-col items-center space-y-3 py-[5vw]">
            <img src="../assets/images/emptySaleItems.png" alt="EmptySaleItems" class=" w-[10vw]">
            <p class="text-[1.5vw] text-[#ABBCC9]">no sale item</p>
        </div>
    </div>
</template>
 
<style scoped>

</style>
