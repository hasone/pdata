<tr>
    <td>
        红包总额(<span class="red">*</span>):
    </td>

    <td>
        <input type="text" name="volume" value="${(entRedpacket.totalVolume)!}"/> M
    </td>
</tr>

<tr>
    <td>
        红包数量(<span class="red">*</span>):
    </td>

    <td>
        <input type="text" name="totalCount" value="${(entRedpacket.totalCount)!}"/> 个
    </td>
</tr>

<tr>
    <td>
        有效期(<span class="red">*</span>):
    </td>

    <td>
        <input type="text" name="validality" value="${(entRedpacket.validality)!}"/> 天
    </td>
</tr>

<tr>
    <td>
        发放总额 :
    </td>

    <td>
        <input type="text" name="total" readonly="readonly" value="${(entRedpacket.totalVolume)!}"/>
    </td>
</tr>