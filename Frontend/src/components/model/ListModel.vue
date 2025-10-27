<script setup>
import { postData } from '@/libs/fetchUtils'
import router from '@/router'
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore()
const { addToCart, getAccessToken, getUserId, isLoggedIn } = userStore

const props = defineProps({
    items: {
        type: Array,
        required: true
    },
    view: {
        type: String,
        default: 'gallery',
        validator: (value) => ['gallery', 'list'].includes(value)
    },
    images: Array
})

const baseUrl = import.meta.env.VITE_APP_URL

const addItemToCart = async (item) => {
    if (!isLoggedIn()) {
        router.push({ name : 'SignIn'})
        return
    }
    try {
        const addedItem = await postData(
            `${import.meta.env.VITE_APP_URL}/v2/carts`, 
            {
                userId: getUserId(),
                saleItemId: item.id,
                model: item.model,
                brandName: item.brandName,
                color: item.color,
                storageGb : item.storageGb,
                quantity: 1,
                maxQuantity: item.quantity,
                priceEach: item.price
            }, 
            getAccessToken())
        addToCart(addedItem)
    } catch (error) {
        console.log(error);
    }
} 

</script>

<template>
    <div :class="view === 'gallery' ? 'grid grid-cols-2 md:grid-cols-4 gap-5 mx-auto' : 'flex flex-col gap-7'">
        <div v-for="(item, index) in items" :key="item.id" class="itbms-row relative shadow-[0_0.065rem_0.18rem_0_rgba(0,0,0,0.15)] rounded-md overflow-hidden hover:shadow-[0_0.08rem_0.4rem_rgba(0,0,0,0.15)] bg-white"
            :class="view === 'gallery' ? 'hover:scale-[1.01]' : 'flex flex-row'" >
            <router-link :to="{ name: 'SaleItemsDetail', params: { saleItemId: item.id } }">
                <div class="flex items-center justify-center bg-[#FAF6F5] h-34 lg:h-42 xl:h-50">
                    <p v-if="!images[index]" class="text-[#332A1E] text-sm lg:text-base xl:text-lg">No Picture</p>
                    <img v-else :src="`${baseUrl}/v1/files/${images[index]}?t=${Date.now()}`" class="h-18 md:h-18 lg:h-24 xl:h-30 my-6 lg:my-8" />
                </div>
                <div class="flex flex-col justify-center p-3 bg-white">
                    <slot name="saleItem" :itemInList="item"/>
                </div>
            </router-link>
            <div v-if="item.quantity > 0 && item.seller.id !== getUserId()" @click="addItemToCart(item)" class="itbms-add-to-cart-button rounded-full p-2 absolute top-3 right-3 bg-[#ABBCC9] hover:bg-[#6F879C]">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="w-4 lg:w-5 xl:w-6 h-auto">
                    <path fill="#FFFFFF" d="M17 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2M1 2v2h2l3.6 7.59l-1.36 2.45c-.15.28-.24.61-.24.96a2 2 0 0 0 2 2h12v-2H7.42a.25.25 0 0 1-.25-.25q0-.075.03-.12L8.1 13h7.45c.75 0 1.41-.42 1.75-1.03l3.58-6.47c.07-.16.12-.33.12-.5a1 1 0 0 0-1-1H5.21l-.94-2M7 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2" />
                </svg>
            </div>
            <div v-else-if="item.quantity === 0" class="absolute top-3 right-3 bg-[#EAA9A9] px-2 py-0.5 rounded-2xl">
                <p class="text-sm lg:text-base text-[#680D0D] font-medium">Out of stock</p>
            </div>
        </div>
    </div>
</template>

<style scoped></style>
