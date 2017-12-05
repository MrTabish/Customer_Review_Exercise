/*    */ package de.hybris.platform.customerreview.impl;
/*    */ 
/*    */ import de.hybris.platform.core.model.c2l.LanguageModel;
/*    */ import de.hybris.platform.core.model.product.ProductModel;
/*    */ import de.hybris.platform.core.model.user.UserModel;
/*    */ import de.hybris.platform.customerreview.CustomerReviewService;
/*    */ import de.hybris.platform.customerreview.dao.CustomerReviewDao;
/*    */ import de.hybris.platform.customerreview.jalo.CustomerReview;
/*    */ import de.hybris.platform.customerreview.jalo.CustomerReviewManager;
/*    */ import de.hybris.platform.customerreview.model.CustomerReviewModel;
/*    */ import de.hybris.platform.jalo.product.Product;
/*    */ import de.hybris.platform.jalo.user.User;
/*    */ import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
/*    */ import de.hybris.platform.servicelayer.model.ModelService;
/*    */ import de.hybris.platform.servicelayer.util.ServicesUtil;

/*    */ import java.util.ArrayList;
/*    */ import java.util.List;

import javax.annotation.Resource;

/*    */ import org.springframework.beans.factory.annotation.Required;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultCustomerReviewService
/*    */   extends AbstractBusinessService
/*    */   implements CustomerReviewService
/*    */ {
/*    */   private CustomerReviewDao customerReviewDao;
/*    */   
/*    */   protected CustomerReviewDao getCustomerReviewDao()
/*    */   {
/* 41 */     return this.customerReviewDao;
/*    */   }
/*    */   
/*    */   @Required
/*    */   public void setCustomerReviewDao(CustomerReviewDao customerReviewDao)
/*    */   {
/* 47 */     this.customerReviewDao = customerReviewDao;
/*    */   }
/*    */   
		   @Resource
		   private CustomerCommentReview customerCommentReview;
		   
/*    */ 
/*    */ 
/*    */   public CustomerReviewModel createCustomerReview(Double rating, String headline, String comment, UserModel user, ProductModel product)
/*    */   {
			checkComment();
/* 54 */     CustomerReview review = CustomerReviewManager.getInstance().createCustomerReview(rating, headline, comment, 
/* 55 */       (User)getModelService().getSource(user), (Product)getModelService().getSource(product));
/* 56 */     return (CustomerReviewModel)getModelService().get(review);
/*    */   }
/*    */   
/*    */ 
/*    */   public void updateCustomerReview(CustomerReviewModel model, UserModel user, ProductModel product)
/*    */   {
/* 62 */     model.setProduct(product);
/* 63 */     model.setUser(user);
/* 64 */     getModelService().save(model);
/*    */   }
/*    */   
/*    */ 
/*    */   public List<CustomerReviewModel> getAllReviews(ProductModel product)
/*    */   {
/* 70 */     List<CustomerReview> reviews = CustomerReviewManager.getInstance().getAllReviews(
/* 71 */       (Product)getModelService().getSource(product));
/* 72 */     return (List)getModelService().getAll(reviews, new ArrayList());
/*    */   }
/*    */   
/*    */ 
/*    */   public Double getAverageRating(ProductModel product)
/*    */   {
/* 78 */     return CustomerReviewManager.getInstance().getAverageRating((Product)getModelService().getSource(product));
/*    */   }
/*    */   
/*    */ 
/*    */   public Integer getNumberOfReviews(ProductModel product)
/*    */   {
/* 84 */     return CustomerReviewManager.getInstance().getNumberOfReviews((Product)getModelService().getSource(product));
/*    */   }
/*    */   
/*    */ 
/*    */   public List<CustomerReviewModel> getReviewsForProduct(ProductModel product)
/*    */   {
/* 90 */     ServicesUtil.validateParameterNotNullStandardMessage("product", product);
/* 91 */     return getCustomerReviewDao().getReviewsForProduct(product);
/*    */   }
/*    */   
/*    */ 
/*    */   public List<CustomerReviewModel> getReviewsForProductAndLanguage(ProductModel product, LanguageModel language)
/*    */   {
/* 97 */     ServicesUtil.validateParameterNotNullStandardMessage("product", product);
/* 98 */     ServicesUtil.validateParameterNotNullStandardMessage("language", language);
/* 99 */     return getCustomerReviewDao().getReviewsForProductAndLanguage(product, language);
/*    */   }
          //check comment based on curse word list and rating
			public static void checkComment(String comment,Double rating) throws JaloInvalidParameterException {
				//get word list
				String curseWordList = customerCommentReview.getCommentWord();
				String[] curseWordArray = curseWordList.split(',');
				boolean checkStatus = false;
				//checked curse word in comment String
				for (String check : curseWordArray) {
					if (comment.contains(check)) {
						checkStatus = true;
						break;
					}
				}
				if (checkStatus) {
					  throw new JaloInvalidParameterException("Please remove curse word from comment", 0);
				}
				//rating check
				checkRating(rating);
			}
			
			public static void checkRating(Double rating) throws JaloInvalidParameterException{
				//throw error if rating less then 0 or given condition
				if (rating < 0) {
					  throw new JaloInvalidParameterException("Please add rating in customer review", 0);
				}
			}
			//get Total Reviews For Product On Inclusive Rating 
			 public List<CustomerReviewModel> getReviewsForProductOnInclusiveRating(ProductModel product,double initialRatingRange,double finalRatingRange)
			 {
				 ServicesUtil.validateParameterNotNullStandardMessage("product", product);
				 return getCustomerReviewDao().getTotalReviewsForProductOnInclusiveRating(product,initialRatingRange,fialRatingRange);
			}
			
/*    */ }
		

/* Location:              /Users/TJL4646/CustomerReview_Assignment/customerreviewserver.jar!/de/hybris/platform/customerreview/impl/DefaultCustomerReviewService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */