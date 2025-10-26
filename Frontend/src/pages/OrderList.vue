<script setup>
import OrderCard from '@/components/order/OrderCard.vue'
import { getItemByIdWithToken } from '@/libs/fetchUtils'
import { useUserStore } from '@/stores/UserStore'
import { ref, onMounted } from 'vue'
import emptyOrdersImg from '@/assets/images/emptySaleItems.png'
import Pagination from '@/components/elements/Pagination.vue'
import { useRoute, useRouter } from 'vue-router'

const userStore = useUserStore()
const { getUserId, getAccessToken, isLoggedIn } = userStore
const route = useRoute()
const router = useRouter()
const orders = ref([])
const response = ref({})
const currentPage = ref(1)
const currentSize = ref(3)
const totalPage = ref(0)
const currentTab = ref(route.query.tab || 'completed')
const tabs = ['completed','canceled']

const goToPage = async (page) => {
    currentPage.value = page
    await getAllOrders()
}

async function getAllOrders() {
    if (!isLoggedIn()) return
    
    try {
        response.value = await getItemByIdWithToken(
            `${import.meta.env.VITE_APP_URL}/v2/users/${getUserId()}/orders`, 
            getAccessToken(), 
            currentPage.value - 1,
            currentSize.value,
            currentTab.value
        )
        orders.value = response.value.content
        totalPage.value = response.value.totalPages
        orders.value.forEach(order => {
            order.orderItems.forEach(item => {
                if (item.image) {
                    item.image = `${import.meta.env.VITE_APP_URL}/v1/files/${item.image}?t=${Date.now()}`
                }
            })
        }) 
    } catch (error) {
        console.log(error)
    }
}

onMounted(async () => {
    try {
        await getAllOrders()
    } catch (error) {
        console.log(error)
    }
})

async function changeTab(tab) {
    try {
        currentTab.value = tab
        currentPage.value = 1
        router.replace({ query: { tab: currentTab.value } })
        await getAllOrders()  
    } catch(error) {
        console.log(error);
    }
}
</script>
 
<template>
<div class="font-rubik px-7 md:px-13 lg:px-19 xl:px-26 pb-15 space-y-7 min-h-screen text-[#332A1E] pt-4 md:pt-8 lg:pt-12 bg-white">
    <p class="font-bold text-2xl md:text-4xl mt-20 mb-5">All orders</p>
    <div>
        <button v-for="tab in tabs" :key="tab" @click="changeTab(tab)"
        :class="[
        'pb-2 mr-6 font-semibold transition duration-200 text-sm sm:text-base',
        currentTab === tab ? 'border-b-4 border-[#332A1E]' : 'text-gray-400 hover:text-[#332A1E]',
        tab === 'completed' ? 'itbms-completed-orders-button' :  'itbms-canceled-orders-button'
        ]">
            {{ tab === 'completed' ? 'Completed': 'Canceled' }}
        </button>
    </div>
    <OrderCard v-if="orders.length" :orders="orders" :tab="currentTab"/>
    <div v-else class="flex flex-col items-center space-y-3 py-18">
        <img :src="emptyOrdersImg" alt="EmptySaleItems" class=" w-36">
        <p class="text-xl text-[#ABBCC9]">no order</p>
    </div>
    <Pagination :totalPage="totalPage" :currentPage="currentPage" :currentSize="currentSize" @changePage="goToPage"/>
</div>
</template>
 
<style scoped>

</style>