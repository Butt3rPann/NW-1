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

const saleItems = ref([])
const route = useRoute()
const isShowPopup = ref(false)
const message = ref('')
const selectedSortField = ref('brand.name')
const selectedSortType = ref('none')

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
        <div class="flex justify-between items-center">
                <div class="flex shadow-[0_0.045rem_0.23rem_0_rgba(0,0,0,0.15)] rounded-md py-1 px-2 w-fit gap-4">
                    <button @click="sortSaleItems('none')" class="itbms-brand-none p-2 rounded-full cursor-pointer">
                        <img src="@/assets/images/sort-none.png" alt="Default" class="w-6 h-6" />
                    </button>
                    <button @click="sortSaleItems('asc')" class="itbms-brand-asc p-2 rounded-full cursor-pointer">
                        <img src="@/assets/images/sort-asc.png" alt="Default" class="w-7 h-7" />
                    </button>
                    <button @click="sortSaleItems('desc')" class="itbms-brand-desc p-2 rounded-full cursor-pointer">
                        <img src="@/assets/images/sort-desc.png" alt="Default" class="w-7 h-7" />
                    </button>
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
