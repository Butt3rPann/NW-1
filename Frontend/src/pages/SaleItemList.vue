<script setup>
import { onMounted, ref } from 'vue';
import { deleteItemById, getItems } from '@/libs/fetchUtils';
import ListModel from '@/components/model/ListModel.vue';
import DeleteConfirmation from '@/components/elements/DeleteConfirmation.vue';
import PopupMessage from '@/components/elements/PopupMessage.vue';
import addIcon from '@/assets/images/add.png'
import BaseButton from '@/components/elements/BaseButton.vue';

const saleItems = ref([])

onMounted(async () => {
    try {
        saleItems.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items/list`)
    } catch (error) {
        console.log(error);
    }
})

const isShowPopup = ref(false)

const message = ref('')

const deletedId = ref(null)

function deleteSaleItemById(id) {
    showDelConfirm.value = true
    deletedId.value = id
}

const showDelConfirm = ref(false)

function closeDelConfirm() {
    showDelConfirm.value = false
    deletedId.value = null
}

async function deleteSaleItem(){
    try {
        const status = await deleteItemById(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, deletedId.value)
        if (status === 404) {
            showNotFound.value = true
        } else {
            saleItems.value = saleItems.value.filter(item => item.id !== deletedId.value)
            message.value = "The sale item has been deleted."
            showDelConfirm.value = false
            isShowPopup.value = true
        }
    } catch (error) {
        console.log(error);  
    }
}
</script>
 
<template>
<div>
    <PopupMessage :message="message" :isShowPopup="isShowPopup" class="fixed mt-25"/>
    <div>
        <div class="font-rubik mx-35 mb-15 mt-30">
            <div class="flex justify-between items-center mb-4">
                <p class="text-[3.5rem] font-bold text-[#332A1E]">Sale Items</p>
                <div class="flex gap-3 h-13">
                    <router-link :to="{ name: 'AddSaleItem' }">
                        <BaseButton :icon="addIcon" text="Add Sale Item" textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]" class="itbms-sale-item-add"/>
                    </router-link>
                    <router-link :to="{ name: 'BrandList' }">
                        <BaseButton text="Manage Brand" class="itbms-manage-brand"/>
                    </router-link>
                </div>
            </div>
            <div class="border border-[#CFC8BE] rounded-md overflow-hidden bg-white">
                <ListModel :items="saleItems" view="list">
                    <template #header>
                        <p class="w-[5%]">id</p>
                        <p class="w-[10%]">Brand</p>
                        <p class="w-[26%]">Model</p>
                        <p class="w-[5%]">Ram</p>
                        <p class="w-[7%]">Storage</p>
                        <p class="w-[13%]">Color</p>
                        <p class="w-[10%]">Screen Size</p>
                        <p class="w-[7%]">Price</p>
                        <p class="w-[7%]">Quantity</p>
                        <div class="w-[10%]">Action</div>
                    </template>
                    <template #saleItem="slotProps">
                        <p class="w-[5%]">{{ slotProps.itemInList.id }}</p>
                        <p class="itbms-brand w-[10%]">{{ slotProps.itemInList.brandName }}</p>
                        <p class="itbms-model w-[26%]">{{ slotProps.itemInList.model }}</p>
                        <p class="itbms-ramGb w-[5%]">{{ slotProps.itemInList.ramGb ?? '-' }}</p>
                        <p class="itbms-storageGb w-[7%]">{{ slotProps.itemInList.storageGb ?? '-' }}</p>
                        <p class="itbms-color w-[13%]">{{ slotProps.itemInList.color ?? '-' }}</p>
                        <p class="itbms-screenSizeInch w-[10%]">{{ slotProps.itemInList.screenSizeInch ?? '-' }}</p>
                        <p class="itbms-price w-[7%]">{{ slotProps.itemInList.price.toLocaleString() }}</p>
                        <p class="itbms-quantity w-[7%]">{{ slotProps.itemInList.quantity }}</p>
                        <div class="w-[10%] flex justify-center gap-3">
                            <router-link :to="{ name: 'EditSaleItem', params: { id: slotProps.itemInList.id } }" class="itbms-edit-button border-2 border-[#6F879C] text-[#6F879C] py-1 px-2.5">E</router-link>
                            <p @click="deleteSaleItemById(slotProps.itemInList.id)" class="itbms-delete-button border-2 border-[#D27B7B] text-[#D27B7B] cursor-pointer py-1 px-2.5">D</p>
                        </div>
                    </template>
                </ListModel>
                <div v-if="!saleItems.length" class="flex flex-col items-center space-y-3 py-18">
                    <img src="../assets/images/emptySaleItems.png" alt="EmptySaleItems" class="w-20">
                    <p class="text-xl text-[#ABBCC9]">no sale item</p>
                </div>
            </div>
        </div>
        <DeleteConfirmation v-if="showDelConfirm" @close="closeDelConfirm" message="Do you want to delete this sale item?" @delete="deleteSaleItem"/>
    </div>
</div>
</template>
 
<style scoped>

</style>