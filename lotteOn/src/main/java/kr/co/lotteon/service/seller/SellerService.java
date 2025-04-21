package kr.co.lotteon.service.seller;

import kr.co.lotteon.dto.seller.SellerDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.seller.Seller;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.seller.SellerRepository;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    
    /*
    * 판매자 등록
    * */
    public void saveSeller(UserDTO userDTO, SellerDTO sellerDTO) {

        userDTO.setName(sellerDTO.getCompany());

        //비밀번호 암호화
        String encodedPass = passwordEncoder.encode(userDTO.getPass());
        userDTO.setPass(encodedPass);

        // 유저 등급 설정
        userDTO.setRole("SELLER");

        // 판매자 등급 설정
        sellerDTO.setRank("BRONZE");

        User user = modelMapper.map(userDTO, User.class);
        userRepository.save(user);

        sellerDTO.setUser(userDTO);
        Seller seller = modelMapper.map(sellerDTO, Seller.class);
        sellerRepository.save(seller);

    }
}
