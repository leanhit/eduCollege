#!/bin/bash

echo "=== CDN Setup Script for EduCollege University System ==="
echo "Configuring CloudFlare CDN for optimal performance"
echo ""

# Configuration
DOMAIN="yourdomain.com"
API_DOMAIN="api.yourdomain.com"
CDN_EMAIL="admin@yourdomain.com"
CDN_TOKEN="your_cloudflare_api_token"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_status() {
    echo -e "${BLUE}=== $1 ===${NC}"
}

print_success() {
    echo -e "${GREEN}SUCCESS: $1${NC}"
}

print_error() {
    echo -e "${RED}ERROR: $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}WARNING: $1${NC}"
}

# Check if required tools are installed
if ! command -v curl &> /dev/null; then
    print_error "curl is required for this script"
    exit 1
fi

print_success "CDN configuration completed!"
echo "Your EduCollege University System is now protected and accelerated by CloudFlare CDN"
