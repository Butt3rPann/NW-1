<script setup>
import { onMounted, ref, watch } from 'vue'
import { deleteItemById, getItemByIdWithToken } from '@/libs/fetchUtils'
import DeleteConfirmation from '@/components/elements/DeleteConfirmation.vue'
import PopupMessage from '@/components/elements/PopupMessage.vue'
import addIcon from '@/assets/images/add.png'
import BaseButton from '@/components/elements/BaseButton.vue'
import ErrorMessage from '@/components/elements/ErrorMessage.vue'
import router from '@/router'
import { useRoute } from 'vue-router'
import emptySaleItemsImg from '@/assets/images/emptySaleItems.png'
import { useUserStore } from '@/stores/UserStore'
import productNotFound from '@/assets/images/product-not-found.png'
import Pagination from '@/components/elements/Pagination.vue'
import { storeToRefs } from 'pinia'

const route = useRoute()
const userStore = useUserStore()
const { getUserId, getAccessToken, setSellerOrdersCount, getSellerOrdersCount } = userStore
const { sellerOrdersCount } = storeToRefs(userStore)

const currentPage = ref(1)

const goToPage = async (page) => {
    currentPage.value = page
}

const response = ref({})
const saleItems = ref([])
const totalPage = ref(0)

onMounted(async () => {
    try {
        loadFromSessionStorage()
        await getSaleItems()
        await getSellerOrders()
    } catch (error) {
        console.log(error);
    }
})

async function getSaleItems() {
    try {
        response.value = await getItemByIdWithToken(`${import.meta.env.VITE_APP_URL}/v2/sellers/${getUserId()}/sale-items`, getAccessToken(), currentPage.value - 1)
        saleItems.value = response.value.content
        totalPage.value = response.value.totalPages
    } catch (error) {
        console.log(error)
    }
}

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
        const status = await deleteItemById(`${import.meta.env.VITE_APP_URL}/v2/sale-items`, deletedId.value, userStore.getAccessToken())
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

async function getSellerOrders() {
    try {
        const response = await getItemByIdWithToken(
            `${import.meta.env.VITE_APP_URL}/v2/sellers/${getUserId()}/orders`,
            getAccessToken(),
            0, 
            1
        )
        sellerOrdersCount.value = response.totalElements
    } catch (error) {
        console.log(error)
        setSellerOrdersCount(0)
    }
}


</script>
 
<template>
<div class="bg-white text-[#332A1E] font-rubik">
    <PopupMessage :message="message" :isShowPopup="isShowPopup" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25"/>
    <div v-if="!showNotFound">
        <div class="mx-auto px-7 pb-15 pt-22 md:pt-30 max-w-300">
            <div class="flex flex-col gap-3 sm:flex-row justify-between mb-7">
                <div class="flex items-center justify-center">
                    <p class="text-3xl sm:text-4xl lg:text-5xl font-bold text-[#332A1E]">Sale Items</p>
                    <router-link :to="{ name: 'SaleOrderList' }">    
		                <div class="relative">
                            <p v-if="getSellerOrdersCount() > 0" class="itbms-bag-quantity absolute -top-1.5 -right-1.5 px-[0.50rem] py-[0.30rem] rounded-full bg-[#D27B7B] text-[#F0EDEC] text-xs">{{ getSellerOrdersCount() }}</p>
                            <div class="itbms-bag-button bg-[#6F879C] w-10 p-2 rounded-md ml-4">
                                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier">
                                    <path fill-rule="evenodd" clip-rule="evenodd" d="M16.5285 6C16.5098 5.9193 16.4904 5.83842 16.4701 5.75746C16.2061 4.70138 15.7904 3.55383 15.1125 2.65C14.4135 1.71802 13.3929 1 12 1C10.6071 1 9.58648 1.71802 8.88749 2.65C8.20962 3.55383 7.79387 4.70138 7.52985 5.75747C7.50961 5.83842 7.49016 5.9193 7.47145 6H5.8711C4.29171 6 2.98281 7.22455 2.87775 8.80044L2.14441 19.8004C2.02898 21.532 3.40238 23 5.13777 23H18.8622C20.5976 23 21.971 21.532 21.8556 19.8004L21.1222 8.80044C21.0172 7.22455 19.7083 6 18.1289 6H16.5285ZM8 11C8.57298 11 8.99806 10.5684 9.00001 9.99817C9.00016 9.97438 9.00044 9.9506 9.00084 9.92682C9.00172 9.87413 9.00351 9.79455 9.00718 9.69194C9.01451 9.48652 9.0293 9.18999 9.05905 8.83304C9.08015 8.57976 9.10858 8.29862 9.14674 8H14.8533C14.8914 8.29862 14.9198 8.57976 14.941 8.83305C14.9707 9.18999 14.9855 9.48652 14.9928 9.69194C14.9965 9.79455 14.9983 9.87413 14.9992 9.92682C14.9996 9.95134 14.9999 9.97587 15 10.0004C15 10.0004 15 11 16 11C17 11 17 9.99866 17 9.99866C16.9999 9.9636 16.9995 9.92854 16.9989 9.89349C16.9978 9.829 16.9957 9.7367 16.9915 9.62056C16.9833 9.38848 16.9668 9.06001 16.934 8.66695C16.917 8.46202 16.8953 8.23812 16.8679 8H18.1289C18.6554 8 19.0917 8.40818 19.1267 8.93348L19.86 19.9335C19.8985 20.5107 19.4407 21 18.8622 21H5.13777C4.55931 21 4.10151 20.5107 4.13998 19.9335L4.87332 8.93348C4.90834 8.40818 5.34464 8 5.8711 8H7.13208C7.10465 8.23812 7.08303 8.46202 7.06595 8.66696C7.0332 9.06001 7.01674 9.38848 7.00845 9.62056C7.0043 9.7367 7.00219 9.829 7.00112 9.89349C7.00054 9.92785 7.00011 9.96221 7 9.99658C6.99924 10.5672 7.42833 11 8 11ZM9.53352 6H14.4665C14.2353 5.15322 13.921 4.39466 13.5125 3.85C13.0865 3.28198 12.6071 3 12 3C11.3929 3 10.9135 3.28198 10.4875 3.85C10.079 4.39466 9.76472 5.15322 9.53352 6Z" fill="#ffffff"></path> </g>
                                </svg>                            
                            </div>
                        </div>
                    </router-link>  
                </div>
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
            <Pagination :totalPage="totalPage" :currentPage="currentPage" @changePage="goToPage"/>
        </div>
        <DeleteConfirmation v-if="showDelConfirm" @close="closeDelConfirm" message="Do you want to delete this sale item?" @delete="deleteSaleItem"/>
    </div>
    <ErrorMessage title="Sale Item" description="The requested sale item does not exist." backPathName="SaleItemsList" v-else :img="productNotFound">
        <span>Sale Item</span><br/>
        <span>Not Found</span>
    </ErrorMessage>
</div>
</template>
 
<style scoped>

</style>
