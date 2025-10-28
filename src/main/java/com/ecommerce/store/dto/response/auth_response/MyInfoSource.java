package com.ecommerce.store.dto.response.auth_response;

import com.ecommerce.store.entity.Customer;
import com.ecommerce.store.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyInfoSource {

    private User user;
    private Customer customer;

    public MyInfoSource(User user, Customer customer) {
        this.user = user;
        this.customer = customer;
    }
}
