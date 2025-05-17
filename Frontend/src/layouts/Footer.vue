<script setup>
import pinIcon from '@/assets/images/pin.png'
import phoneIcon from '@/assets/images/phone.png'
import mailIcon from '@/assets/images/mail.png'
import facebookIcon from '@/assets/images/facebookLogo.png'
import twitterIcon from '@/assets/images/twitterLogo.png'
import igIcon from '@/assets/images/igLogo.png'
import youtubeIcon from '@/assets/images/youtubeLogo.png'
import copyrightIcon from '@/assets/images/copyright.png'

const footerSections = [
    {
        title: 'Shop',
        items: ['All Products', 'Promotions', 'New Arrivals']
    },
    {
        title: 'Account',
        items: ['My Account', 'Order History', 'Favorites']
    },
    {
        title: 'Help',
        items: ['Delivery', 'Returns', 'Secure Payments', 'Track an Order', 'FAQ']
    },
    {
        title: 'About Us',
        items: ['Careers', 'Terms & Conditions of Sales', 'Privacy Policy', 'Responsibility']
    }
]

const contactInfo = [
    {
        icon: pinIcon,
        text: '126 ถนนประชาอุทิศ แขวงบางมด เขตทุ่งครุ กรุงเทพฯ 10140'
    },
    {
        icon: phoneIcon,
        text: '02-123-4567'
    },
    {
        icon: mailIcon,
        text: 'support@itbmshop.com'
    }
]

const socialIcons = [facebookIcon, twitterIcon, igIcon, youtubeIcon]
</script>

<template>
    <div class="font-rubik bg-[#6F879C] text-[#F0EDEC] h-fit py-10 px-15">
        <div class="flex justify-between pb-8 border-b-1 border-[#F0EDEC]">
            <div v-for="section in footerSections" :key="section.title">
                <p class="font-bold text-xl mb-3">{{ section.title }}</p>
                <template v-for="item in section.items" :key="item">
                    <router-link v-if="item === 'All Products'" :to="{ name: 'SaleItems' }"
                        class="text-base mb-1 hover:text-[#334362] cursor-pointer transition-colors duration-150 block">
                        {{ item }}
                    </router-link>
                    <p v-else
                        class="text-base font-normal mb-1 hover:text-[#334362] cursor-pointer transition-colors duration-150">
                        {{ item }}
                    </p>
                </template>
            </div>
            <div class="w-60">
                <p class="font-bold text-xl mb-3">Contact Us</p>
                <div v-for="(contact, index) in contactInfo" :key="index" class="flex items-center mb-1">
                    <img :src="contact.icon" alt="Contact" class="mr-3 w-6" />
                    <p class="text-sm font-light">{{ contact.text }}</p>
                </div>
                <div class="flex items-center">
                    <img v-for="(icon, index) in socialIcons" :key="index" :src="icon" alt=""
                        class="mr-3 mt-3 w-10" />
                </div>
            </div>
        </div>
        <div class="flex justify-between items-center mt-7">
            <div class="flex items-center">
                <img :src="copyrightIcon" alt="copyright"></img>
                <p class="text-base font-light ml-2">2025 ITB-MShop. All rights reserved.</p>
            </div>
            <div class="flex items-center">
                <p class="text-base font-light mr-10">Privacy Policy</p>
                <p class="text-base font-light">Terms & Conditions</p>
            </div>
        </div>
    </div>

</template>

<style scoped></style>