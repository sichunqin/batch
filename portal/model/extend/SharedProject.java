package com.dxn.model.extend;

import com.dxn.dto.SharedProjectDTO;
import com.dxn.forms.dto.ComboboxDTO;
import com.dxn.model.entity.SharedProjectEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.ResponseEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.configuration.DxnConfig;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Base_SharedProject")
public class SharedProject extends SharedProjectEntity {

    /**
     * 保存共享项目
     *
     * @param listdto
     * @return
     * @throws Exception
     */
    @SystemService(args = "listdto")
    public ResponseEntity saveShareProject(List<SharedProjectDTO> listdto) throws Exception {
//        String filePath = DxnConfig.getCsfileUploadPath()+"\\"+projectId+"\\ShareProject\\data_ace_data_.txt";
        ResponseEntity re = new ResponseEntity();
       // if (null == listdto) {
//            List<SharedProjectDTO> list = readFileContent(filePath);
            for (int i = 0; i < listdto.size(); i++) {
                if (i == 0) {
                    this.deleteShareProject(listdto.get(i).getAuditworkProjectID());
                }
                SharedProject sharedProject = this.createNew();
                ModelHelper.copyModel(listdto.get(i), sharedProject);
                sharedProject.setPParentID(listdto.get(i).getParentID());
                this.repository().add(sharedProject);

                if (StringUtils.isNoneBlank(listdto.get(i).getSubItems())) {
                    List<SharedProjectDTO> childlist = Framework.parseJsonArray(listdto.get(i).getSubItems(), SharedProjectDTO.class);
                    for (SharedProjectDTO spd:childlist) {
                        SharedProject childSharedProject = this.createNew();
                        ModelHelper.copyModel(spd, childSharedProject);
                        System.err.println("parentId=="+spd.getParentID());
                        childSharedProject.setPParentID(spd.getParentID());
                        this.repository().add(childSharedProject);
                    }
                }

            }
            re.setCode(200);
            return re;
       /* }else{
            re.setCode(403);
            re.setMessages("请求参数不允许为null");
        }*/

       // return  re;
    }

    /**
     * 获取txt文件json格式内容
     * @param fileName
     * @return
     */
    private static List<SharedProjectDTO> readFileContent(String fileName) {
        StringBuffer sbf = new StringBuffer();
        InputStreamReader reader = null;
        try {
            String encoding = "UTF-8"; // 字符编码
            File f = new File(fileName);
            if (f.isFile() && f.exists()) {
                reader = new InputStreamReader(new FileInputStream(f), encoding);
                BufferedReader in = new BufferedReader(reader);
                String line;
                while ((line = in.readLine()) != null) {
                    sbf.append(line);
                }
                reader.close();
                return Framework.parseJsonArray(sbf.toString(), SharedProjectDTO.class);
            }
        } catch (Exception e) {
            Framework.log.info("读取文件内容操作出错");
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return new ArrayList<SharedProjectDTO>();
    }

    /**
     * 根据项目ID查询某一项目下的附件列表
     *
     * @param projectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "projectId")
    public ResponseEntity queryShareProjectList(String projectId) throws Exception {
        List<SharedProject> list = this.stream().where(w -> w.getAuditworkProjectID().equals(projectId)).toList();
        ResponseEntity re = new ResponseEntity();
        re.setCode(200);
        re.setData(list);
        return re;
    }

    /**
     * 根据项目ID删除项目下的附件列表
     *
     * @param projectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "projectId")
    public ResponseEntity deleteShareProject(String projectId) throws Exception {

        List<SharedProject> list = this.stream().where(w -> w.getAuditworkProjectID().equals(projectId)).toList();
        for (SharedProject item : list) {
            item.initialization();
            this.repository().remove(item);
        }

//        String sql = "Delete from Base_SharedProject where managerProjectID=?";
//        List<Object> parameters = new ArrayList<>();
//        parameters.add(projectId);
//        repository().executeCommand(sql, parameters);

        ResponseEntity re = new ResponseEntity();
        re.setCode(200);
        return re;
    }

    @SystemService(args = "clientGuid")
    public byte[] uploadShareProjectFile(String clientGuid) throws Exception {

        List<SharedProject> list = this.stream().where(w -> w.getClientGuid().equals(clientGuid)).toList();
        String file = null;
        for (SharedProject item : list) {
            file = DxnConfig.getCsfileUploadPath()+"\\"+item.getAuditworkProjectID()+"\\ShareProject\\"+item.getFileNewName();
        }
        return inputStream2ByteArray(file);
    }

    private byte[] inputStream2ByteArray(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();
        return data;
    }

    private byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 8];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        byte[] by = out.toByteArray();
        out.close();
        return by;
    }
}
