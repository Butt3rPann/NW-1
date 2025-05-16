<script setup>
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router'
import { ref } from 'vue';
import { editItem, getItemById } from '@/libs/fetchUtils';
import SaleItemForm from './SaleItemForm.vue';
import ItemNotFound from '../elements/ItemNotFound.vue';

const router = useRouter()
const { params: { id } } = useRoute()
const saleItem = ref({})
const prevPath = ref(null)
const prevPathName = ref(null)
const prevParams = ref(null)

const previousPage = () => {
    prevPath.value = router.options.history.state.back
    if (prevPath.value) {
        const resolve = router.resolve(prevPath.value)
        prevPathName.value = resolve.name
        prevParams.value = resolve.params.saleItemId ?? null
    } else {
        prevPathName.value = 'SaleItemsDetail'
        prevParams.value = id
    }
}

onMounted(async () => {
    try {
        saleItem.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, id)
        previousPage()
    } catch (error) {
        console.log(error);
    }
})

const handleEditSaleItem = async (editedItem) => {
    try {
        await editItem(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, id, editedItem)
        if (prevPathName.value === 'SaleItemsList') {
            router.push({ name: prevPathName.value , query: { edited: 'true' }})
        } else {
            router.push({ name: 'SaleItemsDetail', params: { saleItemId: id}, query: { edited: 'true' } })
        }
    } catch (error) {
        console.log(error)
    }
}
</script>

<template>
    <div v-if="saleItem.id" class="px-25 gap-15 pt-35 pb-20 font-rubik bg-white">
        <p class=" font-medium text-lg mb-7">
            <router-link :to="{ name: 'SaleItems' }"><span class="text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
            <span class="text-[#332A1E]/50 mx-3"> > </span>
            <router-link :to="{ name: 'SaleItemsDetail', params: { saleItemId: id } }"><span class="itbms-back-button text-[#6F879C]">{{ saleItem.model }}</span></router-link>
        </p>
        <SaleItemForm @submitAction="handleEditSaleItem" :saleItemData="saleItem" :pathName="prevPathName" :params="prevParams"/>
    </div>
    <ItemNotFound v-else title="Sale Item" description="The requested sale item does not exist." :backPathName="prevPathName === 'SaleItemsList' ? prevPathName : 'SaleItems'" />
</template>

<style scoped></style>