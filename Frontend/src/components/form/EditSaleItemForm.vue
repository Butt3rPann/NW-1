<script setup>
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router'
import { ref } from 'vue';
import { editItem, getItemById } from '@/libs/fetchUtils';
import SaleItemForm from './SaleItemForm.vue';
import SaleItemNotFound from '@/components/sale-item/SaleItemNotFound.vue';

const router = useRouter()

const disabled = ref(false)

const { params: { id } } = useRoute()

const saleItem = ref({})

onMounted(async () => {
    try {
        saleItem.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, id)
    } catch (error) {
        console.log(error);
    }
})

async function editSaleItem(editedItem) {
    try {
        await editItem(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, id, editedItem)
    } catch (error) {
        console.log(error);
    }
}

function isChange(oldValue, newValue) {
    return (
        oldValue.model !== newValue.model ||
        oldValue.brandName !== newValue.brand.name ||
        oldValue.description !== newValue.description ||
        oldValue.price !== newValue.price ||
        oldValue.ramGb !== (newValue.ramGb ?? null) ||
        oldValue.screenSizeInch !== (newValue.screenSizeInch ?? null) ||
        oldValue.quantity !== newValue.quantity ||
        oldValue.storageGb !== (newValue.storageGb ?? null) ||
        oldValue.color !== (newValue.color ?? null)
    )
}

const handleEditSaleItem = async (editedItem) => {
    if (!isChange(saleItem.value, editedItem)) return

    try {
        console.log('fetch');
        await editSaleItem(editedItem)
        disabled.value = true
        router.push({ path: `/sale-items/${id}`, query: { edited: 'true' } })
    } catch (error) {
        console.log(error)
    }
}
</script>

<template>
    <div>
        <SaleItemForm v-if="saleItem.id" @submitAction="handleEditSaleItem" :saleItemData="saleItem"
            :path="`/sale-items/${id}`" :disabled="disabled" />
        <SaleItemNotFound v-else />
    </div>
</template>

<style scoped></style>