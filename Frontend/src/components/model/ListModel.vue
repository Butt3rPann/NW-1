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
        <ul :class="view === 'gallery' ? 'grid grid-cols-5 gap-5 mx-auto' : ''">
            <li v-for="item in items" :key="item.id" class="itbms-row"
                :class="view === 'gallery' ? 'shadow-[0_0.065rem_0.18rem_0_rgba(0,0,0,0.15)] rounded-md overflow-hidden hover:shadow-[0_0.08rem_0.4rem_rgba(0,0,0,0.15)] hover:scale-[1.01] bg-white' : ''">
                <router-link :to="{ name: 'SaleItemsDetail', params: { saleItemId: item.id } }">
                    <div class="flex items-center justify-center bg-[#FAF6F5]">
                        <img :src="'./saleItemImage/demoImg1.png'" class="h-[7.5rem] my-8" />
                    </div>
                    <div class="flex bg-white">
                        <slot name="saleItem" :itemInList="item"/>
                    </div>
                </router-link>
            </li>
        </ul>
    </div>
</template>

<style scoped></style>