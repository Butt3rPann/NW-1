<script setup>
const props = defineProps({
    items: {
        type: Array,
        required: true
    },
    view: {
        type: String,
        default: 'gallery',
        validator: (value) => ['gallery', 'list'].includes(value)
    }
})
</script>

<template>
    <div>
        <ul :class="view === 'gallery' ? 'grid grid-cols-5 gap-[1.5vw] mx-auto' : ''">
            <li v-for="item in items" :key="item.id" class="itbms-row"
                :class="view === 'gallery' ? 'shadow-[0_0.075vw_0.2vw_0_rgba(0,0,0,0.15)] rounded-[0.5vw] overflow-hidden hover:shadow-[0_0.1vw_0.5vw_rgba(0,0,0,0.15)] hover:scale-[1.01]' : ''">
                <router-link :to="{ name: 'SaleItemsDetail', params: { saleItemId: item.id } }"
                    class="block h-full w-full flex-col">
                    <div class="flex items-center justify-center bg-[#FAF6F5]">
                        <img :src="'./saleItemImage/demoImg.png'" class="h-[8vw] my-[2vw]" />
                    </div>
                    <slot name="saleItem" :itemInList="item" />
                </router-link>
            </li>
        </ul>
    </div>
</template>

<style scoped></style>