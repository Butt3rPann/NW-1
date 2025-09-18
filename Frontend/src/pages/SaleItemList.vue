<script setup>
import { onMounted, ref, computed, watch } from 'vue'
import { deleteItemById, getItemByIdWithToken } from '@/libs/fetchUtils'
import DeleteConfirmation from '@/components/elements/DeleteConfirmation.vue'
import PopupMessage from '@/components/elements/PopupMessage.vue'
import addIcon from '@/assets/images/add.png'
import BaseButton from '@/components/elements/BaseButton.vue'
import ItemNotFound from '@/components/elements/ItemNotFound.vue'
import router from '@/router'
import { useRoute } from 'vue-router'
import emptySaleItemsImg from '@/assets/images/emptySaleItems.png'
import { useUserStore } from '@/UserStore'

const route = useRoute()
const userStore = useUserStore()
const { getUserId, getAccessToken } = userStore

const currentPage = ref(1)

const goToPage = async (page) => {
    currentPage.value = page
}
const prevPage = async (isFirstPage) => {
    if (!isFirstPage) {
        currentPage.value -= 1
    }
}
const nextPage = async (isLastPage) => {
    if (!isLastPage) {
        currentPage.value += 1
    }
}
const lastPage = async (totalPage) => {
    currentPage.value = totalPage
}

const response = ref({})
const saleItems = ref([])
const totalPage = ref(0)

onMounted(async () => {
    try {
        loadFromSessionStorage()
        await getSaleItems()
    } catch (error) {
        console.log(error);
    }
})

async function getSaleItems() {
    try {
        response.value = await getItemByIdWithToken(`${import.meta.env.VITE_APP_URL}/v2/seller/${getUserId()}/sale-items`, getAccessToken(), currentPage.value - 1)
        saleItems.value = response.value.content
        totalPage.value = response.value.totalPages
    } catch (error) {
        console.log(error)
    }
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

function loadFromSessionStorage() {
    const pageStore = sessionStorage.getItem('page')
    if (pageStore) {
        currentPage.value = Number(pageStore)
    }
}

const isShowPopup = ref(false)
const message = ref('')

if (route.query.added === 'true') {
    message.value = "The sale item has been successfully added."
    router.replace({ query: { } })
    sessionStorage.setItem('page', 1)
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
        const status = await deleteItemById(`${import.meta.env.VITE_APP_URL}/v2/sale-items`, deletedId.value)
        if (status === 404) {
            showNotFound.value = true
        } else {
            currentPage.value = 1
            message.value = "The sale item has been deleted."
            showDelConfirm.value = false
            isShowPopup.value = true
            setTimeout(() => isShowPopup.value = false, 2500)
        }
    } catch (error) {
        console.log(error);  
    }
}

watch(currentPage, async () => {
    if (currentPage.value !== Number(sessionStorage.getItem('page'))) {
        sessionStorage.setItem('page', currentPage.value)
        await getSaleItems()
    }
})
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
        <DeleteConfirmation v-if="showDelConfirm" @close="closeDelConfirm" message="Do you want to delete this sale item?" @delete="deleteSaleItem"/>
    </div>
    <ItemNotFound title="Sale Item" description="The requested sale item does not exist." backPathName="SaleItemsList" v-else/>
</div>
</template>
 
<style scoped>

</style>