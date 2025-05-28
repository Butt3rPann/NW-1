<script setup>
import { onMounted, ref } from 'vue'
import { deleteItemById, getItems } from '@/libs/fetchUtils'
import DeleteConfirmation from '@/components/elements/DeleteConfirmation.vue'
import PopupMessage from '@/components/elements/PopupMessage.vue'
import addIcon from '@/assets/images/add.png'
import BaseButton from '@/components/elements/BaseButton.vue'
import ItemNotFound from '@/components/elements/ItemNotFound.vue'
import router from '@/router'
import { useRoute } from 'vue-router'
import emptySaleItemsImg from '@/assets/images/emptySaleItems.png'

const route = useRoute()
const saleItems = ref([])

onMounted(async () => {
    try {
        saleItems.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`)
    } catch (error) {
        console.log(error);
    }
})

const isShowPopup = ref(false)
const message = ref('')

if (route.query.added === 'true') {
    message.value = "The sale item has been successfully added."
    router.replace({ query: { } })
    isShowPopup.value = true
    setTimeout(() => isShowPopup.value = false, 2500)
} else if(route.query.edited === 'true'){
    message.value = "The sale item has been updated."
    router.replace({ query: { } })
    isShowPopup.value = true
    setTimeout(() => isShowPopup.value = false, 2500)
}

const deletedId = ref(null)

function deleteSaleItemById(id){
    showDelConfirm.value = true
    deletedId.value = id
}
const showDelConfirm = ref(false)
const showNotFound = ref(false)

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
            setTimeout(() => isShowPopup.value = false, 2500)
        }
    } catch (error) {
        console.log(error);  
    }
}
</script>
 
<template>
<div class="bg-white text-[#332A1E]">
    <PopupMessage :message="message" :isShowPopup="isShowPopup" class="fixed mt-25"/>
    <div v-if="!showNotFound">
        <div class="font-rubik mx-35 pb-15 pt-30">
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
                <table class="w-full">
                    <thead>
                        <tr class="flex bg-[#F9F5F5] font-semibold border-b border-[#CFC8BE] h-16 items-center">
                            <th class="w-[10%]">id</th>
                            <th class="w-[10%]">Brand</th>
                            <th class="w-[25%]">Model</th>
                            <th class="w-[7%]">Ram</th>
                            <th class="w-[10%]">Storage</th>
                            <th class="w-[13%]">Color</th>
                            <th class="w-[10%]">Price</th>
                            <th class="w-[15%]">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="si in saleItems" :key="saleItems.id" class="itbms-row flex items-center border-t border-[#CFC8BE] h-17">
                            <td class="itbms-id w-[10%] text-center">{{ si.id }}</td>
                            <td class="itbms-brand w-[10%] text-center">{{ si.brandName}}</td>
                            <td class="itbms-model w-[25%] text-center">{{ si.model }}</td>
                            <td class="itbms-ramGb w-[7%] text-center">{{ si.ramGb ?? '-' }}</td>
                            <td class="itbms-storageGb w-[10%] text-center">{{ si.storageGb ?? '-' }}</td>
                            <td class="itbms-color w-[13%] text-center">{{ si.color ?? '-' }}</td>
                            <td class="itbms-price w-[10%] text-center">{{ si.price.toLocaleString()}}</td>
                            <td class="w-[15%] ">
                                <div class="flex justify-center gap-3">
                                    <router-link :to="{ name: 'EditSaleItem', params: { id: si.id } }" 
                                        class="itbms-edit-button border-2 border-[#6F879C] text-[#6F879C] py-1 px-2.5 hover:bg-[#6F879C] hover:text-[#F2EDEC]">
                                        E
                                    </router-link>
                                    <p @click="deleteSaleItemById(si.id)" 
                                        class="itbms-delete-button border-2 border-[#D27B7B] text-[#D27B7B] cursor-pointer py-1 px-2.5 hover:bg-[#D27B7B] hover:text-[#F2EDEC]">
                                        D
                                    </p>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div v-if="!saleItems.length" class="flex flex-col items-center space-y-3 py-18">
                    <img :src="emptySaleItemsImg" alt="EmptySaleItems" class="w-20">
                    <p class="text-xl text-[#ABBCC9]">no sale item</p>
                </div>
            </div>
        </div>
        <DeleteConfirmation v-if="showDelConfirm" @close="closeDelConfirm" message="Do you want to delete this sale item?" @delete="deleteSaleItem"/>
    </div>
    <ItemNotFound title="Sale Item" description="The requested sale item does not exist." backPathName="SaleItemsList" v-else/>
</div>
</template>
 
<style scoped>

</style>