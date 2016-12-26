package com.epam.nv.elasticsearch.mapping;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@XmlRootElement(name = "product")
public class ProductRecordRoot {
	@XmlElement
	String sku;
	@XmlElement
	String productId;
	@XmlElement
	String name;
	@XmlElement
	String type;
	@XmlElement
	Date startDate;
	@XmlElement
	Date activeUpdateDate;
	@XmlElement
	Double regularPrice;
	@XmlElement
	Double salePrice;
	@XmlElement
	Boolean onSale;
	@XmlElement
	Date priceUpdateDate;
	@XmlElement
	Boolean digital;
	@XmlElement
	String productTemplate;
	@XmlElementWrapper(name = "categoryPath")
	@XmlElement(name = "category")
	List<ProductCategory> categories;
	@XmlElement
	Long customerReviewCount;
	@XmlElement
	Double customerReviewAverage;
	@XmlElement
	Boolean customerTopRated;
	@XmlElement
	Boolean freeShipping;
	@XmlElement
	Boolean freeShippingEligible;
	@XmlElement
	Boolean inStoreAvailability;
	@XmlElement
	String inStoreAvailabilityText;
	@XmlElement
	Date inStoreAvailabilityUpdateDate;
	@XmlElement
	Date itemUpdateDate;
	@XmlElement
	Double shippingCost;
	@XmlElement
	Boolean specialOrder;
	@XmlElement
	String shortDescription;
	@XmlElement(name = "class")
	String productClass;
	@XmlElement(name = "subclass")
	String productSubclass;
	@XmlElement
	String department;
	@XmlElement
	String manufacturer;
	@XmlElement
	Double weight;
	@XmlElement
	Double shippingWeight;
	@XmlElement
	String longDescription;
	@XmlElementWrapper(name = "includedItemList")
	@XmlElement(name = "includedItem")
	List<String> includedItemList;
	@XmlElementWrapper(name = "features")
	@XmlElement(name = "feature")
	List<String> features;
	@XmlElement
	Long quantityLimit;
	@XmlElementWrapper(name = "bundledIn")
	@XmlElement(name = "sku")
	List<String> bundledIn;

	public Map<String, Object> toMap() {
		Map<String, Object> document = new LinkedHashMap<>();
		document.put("sku", sku);
		document.put("productId", productId);
		document.put("name", name);
		document.put("type", type);
		document.put("startDate", startDate);
		document.put("activeUpdateDate", activeUpdateDate);
		document.put("regularPrice", regularPrice);
		document.put("salePrice", salePrice);
		document.put("onSale", onSale);
		document.put("priceUpdateDate", priceUpdateDate);
		document.put("digital", digital);
		document.put("productTemplate", productTemplate);
		document.put("categories", categories.stream().map(elem -> elem.name).collect(toList()));
		document.put("customerReviewCount", customerReviewCount);
		document.put("customerReviewAverage", customerReviewAverage);
		document.put("customerTopRated", customerTopRated);
		document.put("freeShipping", freeShipping);
		document.put("freeShippingEligible", freeShippingEligible);
		document.put("inStoreAvailability", inStoreAvailability);
		document.put("inStoreAvailabilityText", inStoreAvailabilityText);
		document.put("inStoreAvailabilityUpdateDate", inStoreAvailabilityUpdateDate);
		document.put("itemUpdateDate", itemUpdateDate);
		document.put("shippingCost", shippingCost);
		document.put("specialOrder", specialOrder);
		document.put("shortDescription", shortDescription);
		document.put("productClass", productClass);
		document.put("productSubclass", productSubclass);
		document.put("department", department);
		document.put("manufacturer", manufacturer);
		document.put("weight", weight);
		document.put("shippingWeight", shippingWeight);
		document.put("longDescription", longDescription);
		document.put("includedItemList", includedItemList);
		document.put("features", features);
		document.put("quantityLimit", quantityLimit);
		document.put("bundledIn", bundledIn);
		return document;
	}

	@XmlRootElement
	private static class ProductCategory {
		@XmlElement
		String name;
	}

	@XmlRootElement(name = "products")
	public static class Products {
		@XmlElement(name = "product")
		public List<ProductRecordRoot> productList;

		@Override
		public String toString() {
			return "Products{" +
					"productList=" + productList.get(0) +
					'}';
		}
	}

	@Override
	public String toString() {
		return "ProductRecordRoot{" +
				"sku='" + sku + '\'' +
				", productId='" + productId + '\'' +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				", startDate=" + startDate +
				", activeUpdateDate=" + activeUpdateDate +
				", regularPrice=" + regularPrice +
				", salePrice=" + salePrice +
				", onSale=" + onSale +
				", priceUpdateDate=" + priceUpdateDate +
				", digital=" + digital +
				", productTemplate='" + productTemplate + '\'' +
				", categories=" + categories +
				", customerReviewCount=" + customerReviewCount +
				", customerReviewAverage=" + customerReviewAverage +
				", customerTopRated=" + customerTopRated +
				", freeShipping=" + freeShipping +
				", freeShippingEligible=" + freeShippingEligible +
				", inStoreAvailability=" + inStoreAvailability +
				", inStoreAvailabilityText='" + inStoreAvailabilityText + '\'' +
				", inStoreAvailabilityUpdateDate=" + inStoreAvailabilityUpdateDate +
				", itemUpdateDate=" + itemUpdateDate +
				", shippingCost=" + shippingCost +
				", specialOrder=" + specialOrder +
				", shortDescription='" + shortDescription + '\'' +
				", productClass='" + productClass + '\'' +
				", productSubclass='" + productSubclass + '\'' +
				", department='" + department + '\'' +
				", manufacturer='" + manufacturer + '\'' +
				", weight=" + weight +
				", shippingWeight=" + shippingWeight +
				", longDescription='" + longDescription + '\'' +
				", includedItemList=" + includedItemList +
				", features=" + features +
				", quantityLimit=" + quantityLimit +
				", bundledIn=" + bundledIn +
				'}';
	}

	public static final String jsonMapping = "\n" +
			"\"product\" : {\n" +
			"  \"properties\" : {\n" +
			"    \"activeUpdateDate\" : {\n" +
			"      \"type\" : \"date\"\n" +
			"    },\n" +
			"    \"categories\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"customerReviewAverage\" : {\n" +
			"      \"type\" : \"float\"\n" +
			"    },\n" +
			"    \"customerReviewCount\" : {\n" +
			"      \"type\" : \"long\"\n" +
			"    },\n" +
			"    \"customerTopRated\" : {\n" +
			"      \"type\" : \"boolean\"\n" +
			"    },\n" +
			"    \"department\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"digital\" : {\n" +
			"      \"type\" : \"boolean\"\n" +
			"    },\n" +
			"    \"features\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"freeShipping\" : {\n" +
			"      \"type\" : \"boolean\"\n" +
			"    },\n" +
			"    \"freeShippingEligible\" : {\n" +
			"      \"type\" : \"boolean\"\n" +
			"    },\n" +
			"    \"inStoreAvailability\" : {\n" +
			"      \"type\" : \"boolean\"\n" +
			"    },\n" +
			"    \"inStoreAvailabilityText\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"inStoreAvailabilityUpdateDate\" : {\n" +
			"      \"type\" : \"date\"\n" +
			"    },\n" +
			"    \"includedItemList\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"itemUpdateDate\" : {\n" +
			"      \"type\" : \"date\"\n" +
			"    },\n" +
			"    \"longDescription\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"manufacturer\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"name\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"onSale\" : {\n" +
			"      \"type\" : \"boolean\"\n" +
			"    },\n" +
			"    \"priceUpdateDate\" : {\n" +
			"      \"type\" : \"date\"\n" +
			"    },\n" +
			"    \"productClass\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"index\": \"not_analyzed\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"productId\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"productSubclass\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"index\": \"not_analyzed\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"productTemplate\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"quantityLimit\" : {\n" +
			"      \"type\" : \"long\"\n" +
			"    },\n" +
			"    \"regularPrice\" : {\n" +
			"      \"type\" : \"float\"\n" +
			"    },\n" +
			"    \"salePrice\" : {\n" +
			"      \"type\" : \"float\"\n" +
			"    },\n" +
			"    \"shippingCost\" : {\n" +
			"      \"type\" : \"float\"\n" +
			"    },\n" +
			"    \"shippingWeight\" : {\n" +
			"      \"type\" : \"float\"\n" +
			"    },\n" +
			"    \"shortDescription\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"sku\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    },\n" +
			"    \"specialOrder\" : {\n" +
			"      \"type\" : \"boolean\"\n" +
			"    },\n" +
			"    \"startDate\" : {\n" +
			"      \"type\" : \"date\"\n" +
			"    },\n" +
			"    \"type\" : {\n" +
			"      \"type\" : \"text\",\n" +
			"      \"fields\" : {\n" +
			"        \"keyword\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"ignore_above\" : 256\n" +
			"        }\n" +
			"      }\n" +
			"    }\n" +
			"  }\n" +
			"}";
}
