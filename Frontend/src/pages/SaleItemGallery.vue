<script setup>
import SaleItemCard from '@/components/sale-item/SaleItemCard.vue'
import { getItems } from '@/libs/fetchUtils'
import { onMounted, ref} from 'vue'
import addIcon from '@/assets/images/add.png'
import PopupMessage from '@/components/elements/PopupMessage.vue'
import router from '@/router'
import { useRoute } from 'vue-router'
import BaseButton from '@/components/elements/BaseButton.vue'
import emptySaleItemsImg from '@/assets/images/emptySaleItems.png'
import sortNone from "@/assets/images/sort-none.png"
import sortAsc from "@/assets/images/sort-asc.png"
import sortDesc from "@/assets/images/sort-desc.png"

const saleItems = ref([])
const route = useRoute()
const isShowPopup = ref(false)
const message = ref('')
const selectedSortField = ref('brand.name')
const selectedSortType = ref('none')
const pageSizeOptions = [5, 10, 20]
const pageSize = ref(10)

if (route.query.added === 'true') {
    message.value = "The sale item has been successfully added."
    router.replace({ query: { } })
    isShowPopup.value = true
} else if (route.query.deleted === 'true'){
    message.value = "The sale item has been deleted."
    router.replace({ query: { } })
    isShowPopup.value = true
}

async function getSaleItems() {
    try {
        saleItems.value = await getItems(
            `${import.meta.env.VITE_APP_URL}/v2/sale-items`,
            selectedSortField.value,  
            selectedSortType.value === 'none' ? null : selectedSortType.value 
        )
        saleItems.value = saleItems.value.content
    } catch (error) {
        console.log(error)
    }
}

onMounted(async () => {
    try {
        await getSaleItems()

    } catch (error) {
        console.log(error)
    }
})

const sortSaleItems = async (type) => {
    selectedSortType.value = type
    await getSaleItems()
}
</script>
 
<template>
<div class="bg-white">
    <PopupMessage :message="message" :isShowPopup="isShowPopup" class="fixed mt-25"/>
    <div class="font-rubik mx-35 pb-15 space-y-7 pt-30">
        <div class="flex justify-between items-center">
            <p class="text-[3.5rem] font-bold text-[#332A1E]">Products</p>
            <router-link :to="{ name: 'AddSaleItem' }">
                <BaseButton :icon="addIcon" text="Add Sale Item" textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]" class="itbms-sale-item-add"/>
            </router-link>
        </div>
        <div class="flex items-center">
                <div class="flex shadow-[0_0.045rem_0.23rem_0_rgba(0,0,0,0.15)] rounded-md py-1 px-2 w-fit gap-4">
                    <button @click="sortSaleItems('none')" class="itbms-brand-none p-2 rounded-full cursor-pointer">
                        <img :src="sortNone" alt="Default" class="w-6 h-6" />
                    </button>
                    <button @click="sortSaleItems('asc')" class="itbms-brand-asc p-2 rounded-full cursor-pointer">
                        <img :src="sortAsc" alt="Default" class="w-7 h-7" />
                    </button>
                    <button @click="sortSaleItems('desc')" class="itbms-brand-desc p-2 rounded-full cursor-pointer">
                        <img :src="sortDesc" alt="Default" class="w-7 h-7" />
                    </button>
                </div>
                <div class="flex items-center ml-7 text-lg text-[#332A1E]">
                    <p class="font-bold">Show</p>
                    <select v-model="pageSize" class="ml-3 h-12 w-20 text-[#332A1E] bg-white border border-[#332A1E]/10 rounded-md px-3 shadow-sm focus:outline-none focus:ring-2 focus:ring-[#2684FF] focus:border-[#2684FF] transition duration-200">
                        <option v-for="pageSizeOption in pageSizeOptions" :key="pageSizeOption" :value="pageSizeOption">{{ pageSizeOption }}</option>
                    </select>
                </div>
        </div>
        <SaleItemCard v-if="saleItems.length" :saleItems="saleItems" view="gallery"/>
        <div v-else class="flex flex-col items-center space-y-3 py-18">
            <img :src="emptySaleItemsImg" alt="EmptySaleItems" class=" w-36">
            <p class="text-xl text-[#ABBCC9]">no sale item</p>
        </div>
    </div>
</div>
</template>
 
<style scoped>
</style>
