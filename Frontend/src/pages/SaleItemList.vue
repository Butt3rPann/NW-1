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
<div class="bg-white text-[#332A1E] font-rubik">
    <PopupMessage :message="message" :isShowPopup="isShowPopup" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25"/>
    <div v-if="!showNotFound">
        <div class="mx-auto px-7 pb-15 pt-22 md:pt-30 max-w-300">
            <div class="flex flex-col gap-3 sm:flex-row justify-between mb-7">
                <p class="text-3xl sm:text-4xl lg:text-5xl font-bold text-[#332A1E]">Sale Items</p>
                <div class="flex items-center gap-3">
                    <router-link :to="{ name: 'AddSaleItem' }">
                        <BaseButton :icon="addIcon" text="Add Sale Item" textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]" class="itbms-sale-item-add"/>
                    </router-link>
                    <router-link :to="{ name: 'BrandList' }">
                        <BaseButton text="Manage Brand" class="itbms-manage-brand"/>
                    </router-link>
                </div>
            </div>
            <div class="border border-[#CFC8BE] rounded-md overflow-x-auto bg-white">
                <table class="table-auto w-full">
                    <thead>
                        <tr class="bg-[#F9F5F5] font-semibold border-b border-[#CFC8BE] h-15 text-center text-sm md:text-lg">
                            <th class="pl-5">id</th>
                            <th class="p-5">Brand</th>
                            <th class="p-5">Model</th>
                            <th class="p-5">Ram</th>
                            <th class="p-5">Storage</th>
                            <th class="p-5">Color</th>
                            <th class="p-5">Price</th>
                            <th class="pr-5">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="si in saleItems" :key="saleItems.id" class="itbms-row border-t border-[#CFC8BE] h-15 text-center text-sm md:text-lg">
                            <td class="itbms-id pl-5">{{ si.id }}</td>
                            <td class="itbms-brand p-3">{{ si.brandName}}</td>
                            <td class="itbms-model p-3">{{ si.model }}</td>
                            <td class="itbms-ramGb p-3">{{ si.ramGb ?? '-' }}</td>
                            <td class="itbms-storageGb p-3">{{ si.storageGb ?? '-' }}</td>
                            <td class="itbms-color p-3">{{ si.color ?? '-' }}</td>
                            <td class="itbms-price p-3">{{ si.price.toLocaleString()}}</td>
                            <td class="pr-5">
                                <div class="flex justify-center gap-1 md:gap-3">
                                    <router-link :to="{ name: 'EditSaleItem', params: { id: si.id } }" 
                                        class="itbms-edit-button border-2 border-[#6F879C] text-[#6F879C] py-1 px-2.5 hover:bg-[#6F879C] hover:text-[#F2EDEC]">
                                        E
                                    </router-link>
                                    <p @click="deleteSaleItemById(si.id)" 
                                        class="itbms-delete-button border-2 border-[#D27B7B] text-[#D27B7B] py-1 px-2.5 hover:bg-[#D27B7B] hover:text-[#F2EDEC]">
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