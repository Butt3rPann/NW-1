<script setup>
import SaleItemCard from '@/components/sale-item/SaleItemCard.vue'
import { getItems } from '@/libs/fetchUtils'
import { onMounted, ref, watchEffect } from 'vue'
import addIcon from '@/assets/images/add.png'
import PopupMessage from '@/components/elements/PopupMessage.vue'
import router from '@/router'
import { useRoute } from 'vue-router'
import BaseButton from '@/components/elements/BaseButton.vue'
import emptySaleItemsImg from '@/assets/images/emptySaleItems.png'
import sortNone from "@/assets/images/sort-none.png"
import sortAsc from "@/assets/images/sort-asc.png"
import sortDesc from "@/assets/images/sort-desc.png"
import { useSaleItemGalleryStore } from '@/stores/SaleItemGalleryStore'
import { storeToRefs } from 'pinia'

const saleItems = ref([])
const route = useRoute()
const isShowPopup = ref(false)
const message = ref('')
const selectedSortField = ref('brand.name')
const selectedSortType = ref('none')
const pageSizeOptions = [5, 10, 20]
const pageSize = ref(10)
const brands = ref([])
const showFilter = ref(false)

const SaleItemsStore = useSaleItemGalleryStore()
const { currentPage, currentFilter } = storeToRefs(SaleItemsStore)
const { clearFilter, deleteFilter } = SaleItemsStore

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
            selectedSortType.value === 'none' ? null : selectedSortType.value,
            currentFilter.value,
            currentPage.value
        )
        saleItems.value = saleItems.value.content
    } catch (error) {
        console.log(error)
    }
}

function loadFromSessionStorage() {
    const filterStore = sessionStorage.getItem('filter')
    if (filterStore) {
        currentFilter.value = JSON.parse(filterStore)
    }
}

onMounted(async () => {
    try {
        loadFromSessionStorage()
        await getSaleItems()
        brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`)
        brands.value = brands.value.sort((a, b) => a.name.localeCompare(b.name))
    } catch (error) {
        console.log(error)
    }
})

const sortSaleItems = async (type) => {
    selectedSortType.value = type
    await getSaleItems()
}

watchEffect(async () => {
    await getSaleItems()

    if (currentFilter.value.length === 0) {
        sessionStorage.removeItem('filter')
    } else {
        sessionStorage.setItem('filter', JSON.stringify(currentFilter.value))
    }
})
</script>

<template>
<div class="bg-white text-[#332A1E]">
    <PopupMessage :message="message" :isShowPopup="isShowPopup" class="fixed mt-25"/>
    <div @click="showFilter = false" class="font-rubik mx-35 pb-15 space-y-7 pt-30">
        <div class="flex justify-between items-center">
            <p class="text-[3.5rem] font-bold text-[#332A1E]">Products</p>
            <router-link :to="{ name: 'AddSaleItem' }">
                <BaseButton :icon="addIcon" text="Add Sale Item" textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]" class="itbms-sale-item-add"/>
            </router-link>
        </div>
        <div class="flex items-center justify-between">
            <div>
                <div class="itbms-brand-filter relative h-12 w-120 text-[#332A1E] flex items-center bg-white border border-[#332A1E]/10 rounded-md shadow-sm">
                    <div class="flex gap-2 mx-4 overflow-hidden">
                        <p v-if="!currentFilter.length" class="text-[#AEAAA6]">Filter by brand(s)</p>
                        <div v-for="(filterBrand, index) in currentFilter" class="itbms-filter-item flex border border-[#ABBCC9] px-4 py-1 rounded-3xl">
                            {{ filterBrand }}
                            <button @click.stop="deleteFilter(index)" class="itbms-filter-item-clear ml-2 text-[#ABBCC9] hover:text-[#6F879C] font-bold text-xs">
                                ✕
                            </button>
                        </div>
                    </div>
                    <div class="absolute right-2 flex gap-2 bg-white h-auto">
                        <button v-if="currentFilter.length" @click="clearFilter" class="itbms-brand-filter-clear ml-3 cursor-pointer flex items-center justify-center px-2 py-1 text-[#6F879C]">
                            ✕
                        </button>
                        <button @click.stop="showFilter = !showFilter" class="itbms-brand-filter-button cursor-pointer flex items-center justify-center p-1.5 bg-[#6F879C] rounded-full">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 16 16">
                                <path fill="#FFFFFF" fill-rule="evenodd" d="M2.43 1c-.799 0-1.28.89-.832 1.55l4.4 6.6V14a.5.5 0 0 0 .276.447l3 1.5a.5.5 0 0 0 .723-.447V9.15l4.4-6.6A.996.996 0 0 0 13.565 1h-11.1zm0 1h11.1L9.05 8.72a.5.5 0 0 0-.084.277v5.69l-2-1v-4.69a.5.5 0 0 0-.084-.277L2.402 2z" clip-rule="evenodd" />
                            </svg>
                        </button>
                    </div>
                </div>
                <div @click.stop v-show="showFilter" class="absolute z-10 h-80 overflow-scroll w-120 bg-white border border-[#332A1E]/10 rounded-md px-5 py-3 shadow-sm">
                    <label v-for="brand in brands" :key="brand.name" class="flex items-center gap-4 py-2 cursor-pointer">
                        <input type="checkbox" v-model="currentFilter" :value="brand.name" class="hidden peer">
                        <div class="w-4 h-4 rounded-sm border border-[#ABBCC9] peer-checked:bg-[#6F879C] peer-checked:border-[#6F879C] flex items-center justify-center transition">
                            <svg v-if="currentFilter.includes(brand.name)" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#F2EDEC" class="w-3.5 h-3.5">
                                <path d="M20.285 6.709a1 1 0 0 0-1.414-1.418l-9.9 9.9-4.242-4.243a1 1 0 0 0-1.415 1.414l4.95 4.95a1 1 0 0 0 1.414 0l10.607-10.603z"/>
                            </svg>
                        </div>
                        <span>{{ brand.name }}</span>
                    </label>
                </div>
            </div>
            <div class="flex gap-7">
                <div class="flex items-center text-lg text-[#332A1E]">
                    <p class="font-bold">Show</p>
                    <select v-model="pageSize" class="ml-3 h-12 w-20 text-[#332A1E] bg-white border border-[#332A1E]/10 rounded-md px-3 shadow-sm focus:outline-none focus:ring-2 focus:ring-[#2684FF] focus:border-[#2684FF] transition duration-200">
                        <option v-for="pageSizeOption in pageSizeOptions" :key="pageSizeOption" :value="pageSizeOption">{{ pageSizeOption }}</option>
                    </select>
                </div>
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
