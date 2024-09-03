package goinmul.sportsmanage.service;

import goinmul.sportsmanage.domain.CustomerSupport;
import goinmul.sportsmanage.repository.CustomerSupportRepository;
import goinmul.sportsmanage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerSupportService {

    private final UserRepository userRepository;
    private final CustomerSupportRepository customerSupportRepository;
    @Transactional
    public void saveCustomerPost(CustomerSupport customerSupport) {
        customerSupportRepository.save(customerSupport);
    }

}
