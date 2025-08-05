<script setup>
import SaleItemCard from '@/components/sale-item/SaleItemCard.vue'
import { getItems } from '@/libs/fetchUtils'
import { onMounted, ref, watch, computed} from 'vue'
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
const pageSizeOptions = [5, 10, 20]
const totalPage = ref(0)
const brands = ref([])
const showFilter = ref(false)
const response = ref({})

const currentPage = ref(1)
const currentSize = ref(10)
const currentFilter = ref([])
const currentSort = ref('none')

const goToPage = async (page) => {
    currentPage.value = page
    await getSaleItems()
}
const prevPage = async (isFirstPage) => {
    if (!isFirstPage) {
        currentPage.value -= 1
        await getSaleItems()
    }
}
const nextPage = async (isLastPage) => {
    if (!isLastPage) {
        currentPage.value += 1
        await getSaleItems()
    }
}
const lastPage = async (totalPage) => {
    currentPage.value = totalPage
    await getSaleItems()
}
const resetPage = async() => { 
    currentPage.value = 1 
    await getSaleItems()
}
const changeSort = (type) => {
    currentSort.value = type
    resetPage()
}
const clearFilter = () => { currentFilter.value = [] }
const deleteFilter = (index) => { currentFilter.value.splice(index, 1) }

const pageNumbers = computed(() => {
    const numbers = []

    let startNumber = Math.max(1, currentPage.value - 9)
    const endNumber = Math.min(totalPage.value, startNumber + 9)

    if(endNumber - startNumber < 9) {
        startNumber = Math.max(1, endNumber - 9)
    }

    for(let i = startNumber; i <= endNumber; i++) {
        numbers.push(i)
    }

    return numbers
})

if (route.query.added === 'true') {
    message.value = "The sale item has been successfully added."
    router.replace({ query: { } })
    isShowPopup.value = true
    sessionStorage.setItem('page', 1)
} else if (route.query.deleted === 'true'){
    message.value = "The sale item has been deleted."
    router.replace({ query: { } })
    isShowPopup.value = true
    sessionStorage.setItem('page', 1)
}

const prevPath = router.options.history.state.back
if (prevPath && (router.resolve(prevPath).name !== 'SaleItemsDetail' && router.resolve(prevPath).name !== 'AddSaleItem')) {
    sessionStorage.setItem('page', 1)
}

async function getSaleItems() {
    try {
        response.value = await getItems(
            `${import.meta.env.VITE_APP_URL}/v2/sale-items`,
            selectedSortField.value,  
            currentSort.value === 'none' ? null : currentSort.value,
            currentFilter.value,
            currentPage.value - 1,
            currentSize.value
        )
        totalPage.value = response.value.totalPages
        saleItems.value = response.value.content
    } catch (error) {
        console.log(error)
    }
}

function loadFromSessionStorage() {
    const filterStore = sessionStorage.getItem('filter')
    const pageStore = sessionStorage.getItem('page')
    const sizeStore = sessionStorage.getItem('size')
    const sortStore = sessionStorage.getItem('sortType')

    if (filterStore) {
        currentFilter.value = JSON.parse(filterStore)
    }
    if (sizeStore) {
        currentSize.value = Number(sizeStore)
    }
    if (pageStore) {
        currentPage.value = Number(pageStore)
    }
    if (sortStore) {
        currentSort.value = sortStore
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

watch([currentFilter, currentSort, currentSize], async () => {
    await getSaleItems()
}, { deep: true })

watch(currentFilter, () => {
    if (JSON.stringify(currentFilter.value) !== sessionStorage.getItem('filter')) {
        resetPage()
        sessionStorage.setItem('filter', JSON.stringify(currentFilter.value))
    }
}, { deep: true })

watch(currentPage, () => {
    if (currentPage.value !== Number(sessionStorage.getItem('page'))) {
        sessionStorage.setItem('page', currentPage.value)
    }
})

watch(currentSort, () => {
    if (currentSort.value !== sessionStorage.getItem('sortType')) {
        sessionStorage.setItem('sortType', currentSort.value)
    }
})

watch(currentSize, () => {
    if (currentSize.value !== Number(sessionStorage.getItem('size'))) {
        sessionStorage.setItem('size', currentSize.value)
        resetPage()
    }
})
</script>

<template>
<div @click="showFilter = false" class="bg-white text-[#332A1E]">
    <PopupMessage :message="message" :isShowPopup="isShowPopup" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25"/>
    <div class="font-rubik px-7 md:px-13 lg:px-19 xl:px-26 pb-15 space-y-7 pt-22 md:pt-30">
        <div class="flex flex-col md:flex-row md:justify-between md:items-center">
            <p class="text-3xl sm:text-4xl lg:text-5xl font-bold text-[#332A1E] mb-3 md:mb-0">Products</p>
            <router-link :to="{ name: 'AddSaleItem' }" class="w-fit">
                <BaseButton :icon="addIcon" text="Add Sale Item" textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]" class="itbms-sale-item-add"/>
            </router-link>
        </div>
        <div class="h-10 md:h-11 lg:h-12 flex justify-between">
            <div class="flex shadow-[0_0.045rem_0.23rem_0_rgba(0,0,0,0.15)] rounded-md py-1 px-1.5 md:py-1 md:px-2 w-fit gap-2 md:gap-3 lg:gap-4">
                <button @click="changeSort('none')" :class="['itbms-brand-none p-2 rounded-md cursor-pointer', currentSort === 'none' ? 'bg-[#ece8e5]' : 'bg-[#FFFF]']">
                    <img :src="sortNone" alt="Default" class="w-4.5 h-4.5 md:w-5 md:h-5 lg:w-6 lg:h-6" />
                </button>
                <button @click="changeSort('asc')" :class="['itbms-brand-asc p-2 rounded-md cursor-pointer', currentSort === 'asc' ? 'bg-[#ece8e5]' : 'bg-[#FFFF]']">
                    <img :src="sortAsc" alt="Default" class="w-4.5 h-4.5 md:w-5 md:h-5 lg:w-6 lg:h-6" />
                </button>
                <button @click="changeSort('desc')" :class="['itbms-brand-desc p-2 rounded-md cursor-pointer', currentSort === 'desc' ? 'bg-[#ece8e5]' : 'bg-[#FFFF]']">
                    <img :src="sortDesc" alt="Default" class="w-4.5 h-4.5 md:w-5 md:h-5 lg:w-6 lg:h-6" />
                </button>
            </div>
            <div class="flex items-center text-sm md:text-base lg:text-lg text-[#332A1E]">
                <p class="font-bold">Show</p>
                <select v-model="currentSize" class="itbms-page-size h-full ml-3 w-13 md:w-20 text-[#332A1E] bg-white border border-[#332A1E]/10 rounded-md px-1.5 md:px-3 shadow-sm focus:outline-none focus:ring-2 focus:ring-[#2684FF] focus:border-[#2684FF] transition duration-200">
                    <option v-for="pageSizeOption in pageSizeOptions" :key="pageSizeOption" :value="pageSizeOption">{{ pageSizeOption }}</option>
                </select>
            </div>
        </div>
        <div class="w-full">
            <div @click.stop="showFilter = !showFilter" class="itbms-brand-filter h-10 md:h-11 lg:h-12 gap-2 justify-between relative text-[#332A1E] px-4 flex items-center bg-white border border-[#332A1E]/10 rounded-md shadow-sm">
                <p class="font-bold text-sm md:text-base">Filter:</p>
                <div class="w-full flex gap-2 overflow-auto" style="scrollbar-width: none;">
                    <p v-if="!currentFilter.length" class="text-[#AEAAA6] text-sm md:text-base">Filter by brand(s)</p>
                    <div v-for="(filterBrand, index) in currentFilter" class="itbms-filter-item flex border border-[#ABBCC9] px-4 py-1 rounded-3xl text-sm md:text-base">
                        {{ filterBrand }}
                        <button @click.stop="deleteFilter(index)" class="itbms-filter-item-clear ml-2 text-[#ABBCC9] hover:text-[#6F879C] font-bold text-xs">
                            ✕
                        </button>
                    </div>
                </div>
                <div class="flex gap-2 bg-white h-auto">
                    <button v-if="currentFilter.length" @click="clearFilter" class="itbms-brand-filter-clear ml-1 cursor-pointer flex items-center justify-center px-2 py-1 text-[#6F879C]">
                        ✕
                    </button>
                    <button @click.stop="showFilter = !showFilter" class="itbms-brand-filter-button cursor-pointer flex items-center justify-center w-7.5 h-7.5 md:w-8 md:h-8 lg:w-9 lg:h-9 p-1.5 bg-[#ABBCC9] hover:bg-[#6F879C] rounded-full">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 16 16">
                            <path fill="#FFFFFF" fill-rule="evenodd" d="M2.43 1c-.799 0-1.28.89-.832 1.55l4.4 6.6V14a.5.5 0 0 0 .276.447l3 1.5a.5.5 0 0 0 .723-.447V9.15l4.4-6.6A.996.996 0 0 0 13.565 1h-11.1zm0 1h11.1L9.05 8.72a.5.5 0 0 0-.084.277v5.69l-2-1v-4.69a.5.5 0 0 0-.084-.277L2.402 2z" clip-rule="evenodd" />
                        </svg>
                    </button>
                </div>
            </div>
            <div @click.stop v-if="showFilter" class="h-50 overflow-scroll bg-white border border-[#332A1E]/10 rounded-md px-5 py-3 shadow-sm">
                <label v-for="brand in brands" :key="brand.name" class="flex items-center gap-4 py-1.5 cursor-pointer">
                    <input type="checkbox" v-model="currentFilter" :value="brand.name" class="hidden peer">
                    <div class="w-4 h-4 rounded-sm border border-[#ABBCC9] peer-checked:bg-[#6F879C] peer-checked:border-[#6F879C] flex items-center justify-center transition">
                        <svg v-if="currentFilter.includes(brand.name)" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#F2EDEC" class="w-3.5 h-3.5">
                            <path d="M20.285 6.709a1 1 0 0 0-1.414-1.418l-9.9 9.9-4.242-4.243a1 1 0 0 0-1.415 1.414l4.95 4.95a1 1 0 0 0 1.414 0l10.607-10.603z"/>
                        </svg>
                    </div>
                    <span class="itbms-filter-item text-sm md:text-base">{{ brand.name }}</span>
                </label>
            </div>
        </div>
        <SaleItemCard v-if="saleItems.length" :saleItems="saleItems" view="gallery" class="xl:grid-cols-5"/>
        <div v-else class="flex flex-col items-center space-y-3 py-18">
            <img :src="emptySaleItemsImg" alt="EmptySaleItems" class=" w-36">
            <p class="text-xl text-[#ABBCC9]">no sale item</p>
        </div>
        <div v-show="totalPage > 1" class="flex flex-wrap justify-center items-center gap-2 mt-8">
            <button @click="goToPage(1)" :disabled="currentPage === 1" :class="['itbms-page-first flex items-center justify-center w-8 h-8 md:w-10 md:h-10 rounded-md border',
            currentPage === 1? 'text-gray-400 border-gray-200 cursor-not-allowed': 'text-[#332A1E] border-gray-300 hover:bg-gray-100']">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-4 h-4 md:w-5 md:h-5">
                    <path d="m11 17-5-5 5-5"></path>
                    <path d="m18 17-5-5 5-5"></path>
                </svg>
            </button>
            <button @click="prevPage(response.first)" :disabled="currentPage === 1" :class="['itbms-page-prev flex items-center justify-center w-8 h-8 md:w-10 md:h-10 rounded-md border',
            currentPage === 1? 'text-gray-400 border-gray-200 cursor-not-allowed': 'text-[#332A1E] border-gray-300 hover:bg-gray-100']">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-4 h-4 md:w-5 md:h-5">
                    <path d="m15 18-6-6 6-6"></path>
                </svg>
            </button>
            <button @click="goToPage(number)" v-for="(number, index) in pageNumbers" :key="number" :class="[`itbms-page-${index} flex items-center justify-center w-8 h-8 md:w-10 md:h-10 rounded-md border text-sm md:text-base`,
            currentPage === number? 'bg-[#6F879C] text-white border-[#6F879C]': 'text-[#332A1E] border-gray-300 hover:bg-gray-100']">
                {{ number }}
            </button>
            <button @click="nextPage(response.last)" :disabled="currentPage === totalPage" :class="['itbms-page-next flex items-center justify-center w-8 h-8 md:w-10 md:h-10 rounded-md border',
            currentPage === totalPage? 'text-gray-400 border-gray-200 cursor-not-allowed': 'text-[#332A1E] border-gray-300 hover:bg-gray-100']">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-4 h-4 md:w-5 md:h-5">
                    <path d="m9 18 6-6-6-6"></path>
                </svg>
            </button>
            <button @click="lastPage(totalPage)" :disabled="currentPage === totalPage" :class="['itbms-page-last flex items-center justify-center w-8 h-8 md:w-10 md:h-10 rounded-md border',
            currentPage === totalPage? 'text-gray-400 border-gray-200 cursor-not-allowed': 'text-[#332A1E] border-gray-300 hover:bg-gray-100']">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-4 h-4 md:w-5 md:h-5">
                    <path d="m13 17 5-5-5-5"></path>
                    <path d="m6 17 5-5-5-5"></path>
                </svg>
            </button>
        </div>
    </div>
</div>
</template>
 
<style scoped>
</style>
