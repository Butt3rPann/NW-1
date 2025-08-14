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
const showBrandFilter = ref(false)
const showPriceFilter = ref(false)
const showStorageFilter = ref(false)
const response = ref({})
const storages = ref([])

const currentPage = ref(1)
const currentSize = ref(10)
const currentSort = ref('none')

const currentBrandFilter = ref([])

const currentLowerPriceFilter = ref(null)
const currentUpperPriceFilter = ref(null)
const priceFilterOptions = [
    {lower: 0, upper: 5000},
    {lower: 5001, upper: 10000},
    {lower: 10001, upper: 20000},
    {lower: 20001, upper: 30000},
    {lower: 30001, upper: 40000},
    {lower: 40001, upper: 50000}
]

const choosePriceFilter = (price) => {
    currentLowerPriceFilter.value = price.lower
    currentUpperPriceFilter.value = price.upper
}

const currentStorageFilter = ref([]) 

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
const clearFilter = () => { 
    currentBrandFilter.value = []
    currentLowerPriceFilter.value = null
    currentUpperPriceFilter.value = null
    currentStorageFilter.value = []
}
const deleteBrandFilter = (index) => { 
    currentBrandFilter.value.splice(index, 1) 
}

const deletePriceFilter = () => { 
    currentLowerPriceFilter.value = null
    currentUpperPriceFilter.value = null
}

const deleteStorageFilter = (index) => { 
    currentStorageFilter.value.splice(index,1)
}

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

const saleItemImg = ref([])

async function getSaleItems() {
    try {
        response.value = await getItems(
            `${import.meta.env.VITE_APP_URL}/v2/sale-items`,
            selectedSortField.value,  
            currentSort.value === 'none' ? null : currentSort.value,
            currentBrandFilter.value,
            currentLowerPriceFilter.value,
            currentUpperPriceFilter.value,
            currentStorageFilter.value,
            currentPage.value - 1,
            currentSize.value
        )
        totalPage.value = response.value.totalPages
        saleItems.value = response.value.content
        saleItemImg.value = saleItems.value.map(item => item.saleItemImages[0]?.fileName)

    } catch (error) {
        console.log(error)
    }
}

function loadFromSessionStorage() {
    const brandFilterStore = sessionStorage.getItem('brandFilter')
    const pageStore = sessionStorage.getItem('page')
    const sizeStore = sessionStorage.getItem('size')
    const sortStore = sessionStorage.getItem('sortType')
    const lowerPriceFilterStore = sessionStorage.getItem('lowerPriceFilter')
    const upperPriceFilterStore = sessionStorage.getItem('upperPriceFilter')
    const storageFilterStore = sessionStorage.getItem('storageFilter')

    if (brandFilterStore) {
        currentBrandFilter.value = JSON.parse(brandFilterStore)
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
    if (lowerPriceFilterStore && lowerPriceFilterStore !== 'null' && lowerPriceFilterStore !== '""') {
        currentLowerPriceFilter.value = Number(lowerPriceFilterStore)
    }
    if (upperPriceFilterStore && upperPriceFilterStore !== 'null' && upperPriceFilterStore !== '""') {
        currentUpperPriceFilter.value = Number(upperPriceFilterStore)
    }
    if (storageFilterStore) {
        currentStorageFilter.value = JSON.parse(storageFilterStore)
    }
}

onMounted(async () => {
    try {
        loadFromSessionStorage()
        await getSaleItems()
        brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`)
        brands.value = brands.value.sort((a, b) => a.name.localeCompare(b.name))

        storages.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/storage-size`)
        if (storages.value[0] === null) {
            storages.value.splice(0, 1)
            storages.value.push(null)
        }
    } catch (error) {
        console.log(error)
    }
})

const isFirstRun = ref(true)

watch([currentBrandFilter, currentSort, currentSize, currentLowerPriceFilter, currentUpperPriceFilter, currentStorageFilter], async () => {
    if (isFirstRun) {
        isFirstRun.value = false
        return
    }
    await getSaleItems()
}, { deep: true })

watch(currentBrandFilter, () => {
    if (JSON.stringify(currentBrandFilter.value) !== sessionStorage.getItem('brandFilter')) {
        resetPage()
        sessionStorage.setItem('brandFilter', JSON.stringify(currentBrandFilter.value))
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

watch(currentLowerPriceFilter, () => {
    if (JSON.stringify(currentLowerPriceFilter.value) !== sessionStorage.getItem('lowerPriceFilter')) {
        resetPage()
        sessionStorage.setItem('lowerPriceFilter', JSON.stringify(currentLowerPriceFilter.value))
    }
}, { deep: true })

watch(currentUpperPriceFilter, () => {
    if (JSON.stringify(currentUpperPriceFilter.value) !== sessionStorage.getItem('upperPriceFilter')) {
        resetPage()
        sessionStorage.setItem('upperPriceFilter', JSON.stringify(currentUpperPriceFilter.value))
    }
}, { deep: true })

watch(currentStorageFilter, () => {
    if (JSON.stringify(currentStorageFilter.value) !== sessionStorage.getItem('storageFilter')) {
        resetPage()
        sessionStorage.setItem('storageFilter', JSON.stringify(currentStorageFilter.value))
    }
}, { deep: true })
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
        <div>
            <div class="relative bg-white border border-[#332A1E]/10 rounded-md shadow-sm py-3 w-full grid grid-cols-16 items-center">
                <div @click.stop="showBrandFilter = !showBrandFilter" class="itbms-brand-filter col-span-5 space-y-2 px-3 border-r border-[#332A1E]/20">
                    <div class="font-bold text-sm md:text-base text-center">Brand</div>
                    <div class="w-full flex items-center gap-2">
                        <div class="w-full flex overflow-auto gap-2" style="scrollbar-width: none;"> 
                            <p v-if="!currentBrandFilter.length" class="text-[#AEAAA6] text-sm md:text-base mx-auto">Filter by brand(s)</p>
                            <div v-for="(filterBrand, index) in currentBrandFilter" class="itbms-brand-item whitespace-nowrap inline-flex items-center w-fit border border-[#ABBCC9] px-4 py-1 rounded-3xl text-sm md:text-base">
                                {{ filterBrand }}
                                <button @click.stop="deleteBrandFilter(index)" class="itbms-brand-item-clear ml-2 text-[#ABBCC9] hover:text-[#6F879C] font-bold text-xs">
                                    ✕
                                </button>
                            </div>
                        </div>
                    </div> 
                </div>
                <div @click.stop="showPriceFilter = !showPriceFilter" class="itbms-price-filter col-span-5 space-y-2 px-3 border-r border-[#332A1E]/20">
                    <div class="font-bold text-sm md:text-base text-center">Price</div>
                    <div class="w-full flex items-center gap-2">
                        <div class="w-full flex overflow-auto" style="scrollbar-width: none;"> 
                            <p v-if="(currentLowerPriceFilter === null || currentLowerPriceFilter === '') && (currentUpperPriceFilter === null || currentUpperPriceFilter === '')" class="text-[#AEAAA6] text-sm md:text-base mx-auto">Price Range</p>
                            <div v-else class="itbms-price-item inline-flex items-center w-fit border border-[#ABBCC9] px-4 py-1 rounded-3xl text-sm md:text-base">
                                <p>
                                    <span v-if="currentLowerPriceFilter !== null && currentLowerPriceFilter !== ''">{{ currentLowerPriceFilter.toLocaleString() }}</span>
                                    <span v-else-if="currentUpperPriceFilter !== null && currentUpperPriceFilter !== ''">0</span>
                                    <span v-if="currentUpperPriceFilter !== null && currentUpperPriceFilter !== '' && currentUpperPriceFilter !== currentLowerPriceFilter"> {{ ` - ${currentUpperPriceFilter.toLocaleString()}` }} </span>
                                </p>
                                <button @click.stop="deletePriceFilter" class="itbms-price-item-clear ml-2 text-[#ABBCC9] hover:text-[#6F879C] font-bold text-xs">
                                    ✕
                                </button>
                            </div>
                        </div>
                    </div> 
                </div>
                <div @click.stop="showStorageFilter = !showStorageFilter" class="itbms-storage-size-filter px-3 space-y-2 col-span-5">
                    <div class="font-bold text-sm md:text-base text-center">Storage</div>
                    <div class="w-full flex items-center gap-2">
                        <div class="w-full flex overflow-auto gap-2" style="scrollbar-width: none;"> 
                            <p v-if="!currentStorageFilter.length" class="text-[#AEAAA6] text-sm md:text-base mx-auto">Storage size(s)</p>
                            <div v-for="(filterStorage, index) in currentStorageFilter" class="itbms-storage-size-item whitespace-nowrap inline-flex items-center w-fit border border-[#ABBCC9] px-4 py-1 rounded-3xl text-sm md:text-base">
                                {{ filterStorage ? `${filterStorage}Gb` : 'Not specified' }}
                                <button @click.stop="deleteStorageFilter(index)" class="itbms-storage-size-item-clear ml-2 text-[#ABBCC9] hover:text-[#6F879C] font-bold text-xs">
                                    ✕
                                </button>
                            </div>
                        </div>
                    </div> 
                </div>
                <button @click.stop="clearFilter(index)" class="itbms-filter-clear cursor-pointer bg-[#ABBCC9] hover:bg-[#6F879C] text-white rounded-md h-fit w-fit p-3">clear</button>
            </div>
            <div class="w-full grid grid-cols-16">
                <div @click.stop v-if="showBrandFilter" class="h-50 col-span-5 overflow-scroll bg-white border border-[#332A1E]/10 rounded-md px-5 py-3 shadow-sm text-sm lg:text-base">
                    <label v-for="brand in brands" :key="brand.name" class="flex items-center gap-4 py-1.5 cursor-pointer">
                        <input type="checkbox" v-model="currentBrandFilter" :value="brand.name" class="hidden peer">
                        <div class="w-4 h-4 flex-shrink-0 rounded-sm border border-[#ABBCC9] peer-checked:bg-[#6F879C] peer-checked:border-[#6F879C] flex items-center justify-center transition">
                            <svg v-if="currentBrandFilter.includes(brand.name)" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#F2EDEC" class="w-3.5 h-3.5">
                                <path d="M20.285 6.709a1 1 0 0 0-1.414-1.418l-9.9 9.9-4.242-4.243a1 1 0 0 0-1.415 1.414l4.95 4.95a1 1 0 0 0 1.414 0l10.607-10.603z"/>
                            </svg>
                        </div>
                        <span class="itbms-brand-item">{{ brand.name }}</span>
                    </label>
                </div>
                <div @click.stop v-if="showPriceFilter" class="h-50 col-span-5 col-start-6 overflow-scroll bg-white border border-[#332A1E]/10 rounded-md px-5 py-3 shadow-sm text-sm lg:text-base">
                    <label @click="choosePriceFilter(price)" v-for="(price, index) in priceFilterOptions" :key="index" class="itbms-price-item flex justify-center items-center gap-4 py-1.5 hover:bg-[#e7edf2] cursor-pointer">
                        <span class="itbms-filter-item">{{ `${price.lower.toLocaleString()} - ${price.upper.toLocaleString()} Baht` }}</span>
                    </label>
                    <div class="flex gap-3 justify-center items-center py-1.5">
                        <input type="number" v-model="currentLowerPriceFilter" placeholder="Min Price" class="itbms-price-item-min shadow-sm border border-[#332A1E]/10 md:w-15 lg:w-20 xl:w-30 placeholder:text-xs lg:placeholder:text-sm p-1">
                        <input type="number" v-model="currentUpperPriceFilter" placeholder="Max Price" class="itbms-price-item-max shadow-sm border border-[#332A1E]/10 md:w-15 lg:w-20 xl:w-30 placeholder:text-xs lg:placeholder:text-sm p-1">
                        <p>Baht</p>
                    </div>
                </div>
                <div @click.stop v-if="showStorageFilter" class="h-50 col-span-5 col-start-11 overflow-scroll bg-white border border-[#332A1E]/10 rounded-md px-5 py-3 shadow-sm text-sm lg:text-base">
                    <label v-for="(storage, index) in storages" :key="index" class="flex items-center gap-4 py-1.5 cursor-pointer">
                        <input type="checkbox" v-model="currentStorageFilter" :value="storage?.storageGb ?? null" class="hidden peer">
                        <div class="w-4 h-4 flex-shrink-0 rounded-sm border border-[#ABBCC9] peer-checked:bg-[#6F879C] peer-checked:border-[#6F879C] flex items-center justify-center transition">
                            <svg v-if="currentStorageFilter.includes(storage?.storageGb) || currentStorageFilter.includes(null)" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#F2EDEC" class="w-3.5 h-3.5">
                                <path d="M20.285 6.709a1 1 0 0 0-1.414-1.418l-9.9 9.9-4.242-4.243a1 1 0 0 0-1.415 1.414l4.95 4.95a1 1 0 0 0 1.414 0l10.607-10.603z"/>
                            </svg>
                        </div>
                        <span class="itbms-storage-size-item">{{ storage?.storageGb ? `${storage?.storageGb}Gb` : 'Not specified' }}</span>
                    </label>
                </div>
            </div>
        </div>
        <SaleItemCard v-if="saleItems.length" :saleItems="saleItems" view="gallery" :images="saleItemImg" class="xl:grid-cols-5"/>
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
