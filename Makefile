#

STRIPE_BIN:=/app/bin/stripe
TMP_PRODUCT_JSON=/tmp/product.json

stripe-shell:
	docker build -t stripe-shell .
	docker run --rm -it -v ${PWD}:/app/work --entrypoint bash stripe-shell

define createProduct
	${STRIPE_BIN} products create \
		--name=$1 --description="test Product" | tee ${TMP_PRODUCT_JSON}
	cat ${TMP_PRODUCT_JSON} | jq ".id"
	${STRIPE_BIN} prices create --currency=jpy --unit-amount=$2 --product=$$(cat ${TMP_PRODUCT_JSON} | jq -r ".id")
endef

login:
	${STRIPE_BIN} login

add-product:
	$(call createProduct,"のり",300)
	$(call createProduct,"鉛筆",200)
	$(call createProduct,"赤ペン",100)
