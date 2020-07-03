package com.dxn.model.extend;

import com.dxn.model.entity.NoFunctionalUploadControlEntity;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Base_NoFunctionalUploadControl")
public class NoFunctionalUploadControl extends NoFunctionalUploadControlEntity {

    @SystemService()
    public Long outputData() throws Exception {
      Optional<NoFunctionalUploadControl> noFunctionalUploadControlFirst=this.stream().findFirst();
      if (noFunctionalUploadControlFirst.isPresent()){
          NoFunctionalUploadControl noFunctionalUploadControl=noFunctionalUploadControlFirst.get();
          return noFunctionalUploadControl.getId();
      }
        return null;
    }
}
