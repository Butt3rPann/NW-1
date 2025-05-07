<script setup>
import { useRouter } from 'vue-router'
import { ref, onMounted } from "vue"
import SaleItemForm from '@/components/form/SaleItemForm.vue'
import { getSaleItems, addSaleItem } from '@/libs/fetchUtils'

const router = useRouter()
const saleItems = ref([])

onMounted(async () => {
    try {
        saleItems.value = await getSaleItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`)
    } catch (error) {
        console.log(error);
    }
})

const addNewSaleItem = async (newSaleItemData) => {
    try {
        const newSaleItem = await addSaleItem(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, newSaleItemData)
        if(newSaleItem) {
            saleItems.value.push(newSaleItem)
        }
    } catch (error) {
        console.log(error);
    }
}

const handleNewSaleItem = async (newSaleItem) => {
    await addNewSaleItem(newSaleItem);
    router.push('/sale-items');
}
</script>

<template>
    <div>
        <SaleItemForm :submitAction="handleNewSaleItem"></SaleItemForm>
    </div>
</template>

<style scoped></style>